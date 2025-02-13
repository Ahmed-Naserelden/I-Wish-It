package client;

import java.io.File;
import javafx.scene.image.Image;
import connection.MemberPayload;

public class DataStore {

    private static Image memberImage;
    private static MemberPayload member;
    private static int effectiveMemberId;
    private static String effectiveMemberName;
    private static String effectiveMemberDob;
    private static boolean InsideMarketPlace = false;

    public static Image getMemberImage(String filename) {
        String filepath = "../cdn/members/" + filename;
        File imageFile = new File(filepath);
        if (!filename.isEmpty() && imageFile.exists()) {
            return new Image(imageFile.toURI().toString());
        } else {
            return new Image("scene/img/placeholder/user.png");
        }
    }

    public static Image getProductImage(String filename) {
        String filepath = "../cdn/products/" + filename;
        File imageFile = new File(filepath);
        if (imageFile.exists()) {
            return new Image(imageFile.toURI().toString());
        } else {
            return new Image("scene/img/placeholder/item.png");
        }
    }

    public static void setMember(MemberPayload member) {
        memberImage = getMemberImage(member.getImage());
        DataStore.member = member;
    }

    public static MemberPayload getMember() {
        return DataStore.member;
    }

    public static void setEffectiveMember(int id, String name, String dob) {
        effectiveMemberId = id;
        effectiveMemberName = name;
        effectiveMemberDob = dob;
    }

    public static int getEffectiveMemberId() {
        return DataStore.effectiveMemberId;
    }

    public static String getEffectiveMemberName() {
        return effectiveMemberName;
    }

    public static String getEffectiveMemberDob() {
        return effectiveMemberDob;
    }

    public static Image getMemberImage() {
        return memberImage;
    }

    public static boolean isInsideMarketPlace() {
        return InsideMarketPlace;
    }

    public static void setInsideMarketPlace(boolean InsideMarketPlace) {
        DataStore.InsideMarketPlace = InsideMarketPlace;
    }

}
