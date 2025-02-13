@ECHO OFF
CLS

ECHO Download Dummy Products and SQL Insertion Values:
ECHO -------------------------------------------------

python "python\fetch_products.py" --sql=..\..\database --images=..\..\cdn\products

PAUSE