package client;

import java.io.File;
import javafx.scene.image.Image;
import connection.MemberPayload;

/**
 * DataStore class to manage application data.
 * Handles member information, images, and marketplace state.
 */
public class DataStore {

    private static Image memberImage;
    private static MemberPayload member;
    private static int effectiveMemberId;
    private static String effectiveMemberName;
    private static String effectiveMemberDob;
    private static boolean InsideMarketPlace = false;

    /**
     * Retrieves the member image.
     *
     * @param filename The filename of the member image.
     * @return The Image object of the member.
     */
    public static Image getMemberImage(String filename) {
        String filepath = "../cdn/members/" + filename;
        File imageFile = new File(filepath);
        if (!filename.isEmpty() && imageFile.exists()) {
            return new Image(imageFile.toURI().toString());
        } else {
            return new Image("scene/img/placeholder/user.png");
        }
    }

    /**
     * Retrieves the product image.
     *
     * @param filename The filename of the product image.
     * @return The Image object of the product.
     */
    public static Image getProductImage(String filename) {
        String filepath = "../cdn/products/" + filename;
        File imageFile = new File(filepath);
        if (imageFile.exists()) {
            return new Image(imageFile.toURI().toString());
        } else {
            return new Image("scene/img/placeholder/item.png");
        }
    }

    /**
     * Sets the member information.
     *
     * @param member The MemberPayload object containing member information.
     */
    public static void setMember(MemberPayload member) {
        memberImage = getMemberImage(member.getImage());
        DataStore.member = member;
    }

    /**
     * Retrieves the member information.
     *
     * @return The MemberPayload object containing member information.
     */
    public static MemberPayload getMember() {
        return DataStore.member;
    }

    /**
     * Sets the effective member information.
     *
     * @param id The ID of the effective member.
     * @param name The name of the effective member.
     * @param dob The date of birth of the effective member.
     */
    public static void setEffectiveMember(int id, String name, String dob) {
        effectiveMemberId = id;
        effectiveMemberName = name;
        effectiveMemberDob = dob;
    }

    /**
     * Retrieves the effective member ID.
     *
     * @return The ID of the effective member.
     */
    public static int getEffectiveMemberId() {
        return DataStore.effectiveMemberId;
    }

    /**
     * Retrieves the effective member name.
     *
     * @return The name of the effective member.
     */
    public static String getEffectiveMemberName() {
        return effectiveMemberName;
    }

    /**
     * Retrieves the effective member date of birth.
     *
     * @return The date of birth of the effective member.
     */
    public static String getEffectiveMemberDob() {
        return effectiveMemberDob;
    }

    /**
     * Retrieves the member image.
     *
     * @return The Image object of the member.
     */
    public static Image getMemberImage() {
        return memberImage;
    }

    /**
     * Checks if the user is inside the marketplace.
     *
     * @return True if the user is inside the marketplace, false otherwise.
     */
    public static boolean isInsideMarketPlace() {
        return InsideMarketPlace;
    }

    /**
     * Sets the marketplace state.
     *
     * @param InsideMarketPlace True if the user is inside the marketplace, false otherwise.
     */
    public static void setInsideMarketPlace(boolean InsideMarketPlace) {
        DataStore.InsideMarketPlace = InsideMarketPlace;
    }
}
