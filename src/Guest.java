public class Guest {
    private int guestId;
    private String name;
    private String contactInfo;

    public Guest(int guestId, String name, String contactInfo) {
        this.guestId = guestId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "Guest ID: " + guestId + "\nName: " + name + "\nContact Info: " + contactInfo;
    }
}