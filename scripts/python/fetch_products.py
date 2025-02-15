import os
import requests
from concurrent.futures import ThreadPoolExecutor


def get_all_products() -> tuple[list[tuple[int, str, str, str, int, str]], list[str]]:
    DUMMY_PRODUCTS_URL = "https://dummyjson.com/products?limit=0"
    PRODUCT_IMAGE = "product_{index:03d}.png"

    try:
        response = requests.get(DUMMY_PRODUCTS_URL)
    except requests.ConnectionError:
        print("[EXIT] Connection failure")
        exit(1)

    if response.status_code != 200:
        print(f"[FAIL] Can't get all products [ERC={response.status_code}]")
        return ([], [])

    data: list[dict] = response.json()["products"]

    products = [
        (
            index,  # PRODUCT_ID
            product["title"],  # NAME
            product.get("brand", "Generic"),  # BRAND
            product["description"],  # INFO
            round(product["price"] * 4) * 25,  # PRICE
            PRODUCT_IMAGE.format(index=index),  # IMAGE
        )
        for index, product in enumerate(data, start=1)
    ]

    thumbnails = [item["thumbnail"] for item in data]

    return (products, thumbnails)


def download_images(thumbnails: list[str], img_dir: str):
    count = {"exist": 0, "download": 0, "failed": 0, "total": 0}

    def download_image(image_url: str, filename: str) -> None:
        try:
            extension = image_url.split(".")[-1]
            file_path = os.path.join(img_dir, f"{filename}.{extension}")

            if os.path.exists(file_path):
                count["exist"] += 1
                count["total"] += 1
                return

            response = requests.get(image_url, stream=True, timeout=3)
            response.raise_for_status()  # Raise an error for failed requests

            with open(file_path, "wb") as file:
                for chunk in response.iter_content(1024):
                    file.write(chunk)

            cnt = count["total"] + 1
            percent = f"{cnt / len(thumbnails):5.1%}"
            print(
                f"[INFO] Progress: {cnt:03d}/{len(thumbnails):03d} ({percent})",
                end="\r",
            )
            count["download"] += 1
            count["total"] += 1

        except requests.Timeout as _:
            download_image(image_url, filename)
        except requests.RequestException as e:
            print(f"\n[FAIL] Can't download {image_url}: {e}", end="\r")
            count["failed"] += 1
        except requests.ConnectionError:
            print("[EXIT] Connection failure")
            exit(1)

    with ThreadPoolExecutor() as executor:
        executor.map(
            lambda item: download_image(item[1], f"product_{item[0]:03d}"),
            enumerate(thumbnails, start=1),
        )

    if count["exist"] == len(thumbnails):
        print(f"[INFO] All ({count['exist']}) images already exists.")
    elif count["exist"] == 0:
        print(f"\n[INFO] download ({count['download']}) images.")
    else:
        print(
            f"\n[INFO] ({count['exist']}) images existed, "
            f"and ({count['download']}) images downloaded"
        )
    return count["total"] == len(thumbnails)


if __name__ == "__main__":
    import argparse

    os.chdir(os.path.dirname(__file__))  # Make sure CWD is correct

    parser = argparse.ArgumentParser(
        description="Fetch products and create insert SQL query."
    )

    # Define input and output as string arguments
    parser.add_argument("--sql", help="Output for products.sql")
    parser.add_argument("--images", help="Output path for products images")

    args = parser.parse_args()
    sql_dir = args.sql
    img_dir = args.images

    products, thumbnails = get_all_products()

    with open("products.sql-template") as fp:
        template = fp.read()

    products_sql_path = os.path.abspath(f"{sql_dir}/products.sql")
    with open(products_sql_path, "w+") as fp:
        values = "\n    ".join(
            f"({i}, '{n.replace("'", "''")}', '{b.replace("'", "''")}', "
            f"'{d.replace("'", "''")}', {p}, '{im}'),"
            for (i, n, b, d, p, im) in products
        )
        fp.write(template.replace("##PRODUCT_VALUES##", "    " + values[:-1]))
    print(f"[INFO] Created Products SQL Insertion Values: {products_sql_path}")

    os.makedirs(img_dir, exist_ok=True)  # Ensure the directory exists
    print(f'[INFO] Download directory is: "{os.path.abspath(img_dir)}"')

    while not download_images(thumbnails, img_dir):
        pass
