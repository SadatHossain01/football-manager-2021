package DataModel;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private String unAccentedName;
    private String country;
    private String clubName;
    private final String position;
    private final int age;
    private String DoB = "";
    private double weight;
    private String imageSource;
    public enum PreferredFoot {Left, Right, Both};
    private PreferredFoot preferredFoot;
    private int number;
    private double estimatedValue;
    private final double height;
    private final double weeklySalary;
    private boolean isTransferListed;
    private double transferFee;

    public Player(String name, String unAccentedName, String clubName, String country, int age, double estimatedValue, int number, String position, double height, double weight, String pFoot, double weeklySalary, String imageSource, int isTransferListed, double transferFee) {
        this.name = name;
        this.unAccentedName = unAccentedName;
        this.country = country;
        this.clubName = clubName;
        this.position = position;
        this.age = age;
        this.weight = weight;
        this.imageSource = imageSource;
        if (pFoot.equalsIgnoreCase("Left")) this.preferredFoot = PreferredFoot.Left;
        else if (pFoot.equalsIgnoreCase("Right")) this.preferredFoot = PreferredFoot.Right;
        else this.preferredFoot = PreferredFoot.Both;
        this.number = number;
        this.estimatedValue = estimatedValue;
        this.height = height;
        this.weeklySalary = weeklySalary;
        this.isTransferListed = isTransferListed == 1;
        this.transferFee = transferFee;
    }

    public Player(Player player) {
        this.name = player.name;
        this.unAccentedName = player.unAccentedName;
        this.country = player.country;
        this.clubName = player.clubName;
        this.position = player.position;
        this.age = player.age;
        this.number = player.number;
        this.height = player.height;
        this.weeklySalary = player.weeklySalary;
        this.isTransferListed = player.isTransferListed;
        this.transferFee = player.transferFee;
        this.preferredFoot = player.preferredFoot;
        this.weight = player.weight;
        this.DoB = player.DoB;
        this.estimatedValue = player.estimatedValue;
        this.imageSource = player.imageSource;
    }

    public double getEstimatedValue() {
        return estimatedValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = League.FormatName(name);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = League.FormatName(country);
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String ClubName) {
        this.clubName = League.FormatName(ClubName);
    }

    public String getPosition() {
        return position;
    }

    public int getAge() {
        return age;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getHeight() {
        return height;
    }

    public double getWeeklySalary() {
        return weeklySalary;
    }

    public double getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(double transferFee) {
        this.transferFee = transferFee;
    }

    public boolean isTransferListed() {
        return isTransferListed;
    }

    public void setTransferListed(boolean transferListed) {
        isTransferListed = transferListed;
    }

    public double getWeight() {
        return weight;
    }

    public String getImageSource() {
        return imageSource;
    }

    public PreferredFoot getPreferredFoot() {
        return preferredFoot;
    }

    public String getUnAccentedName() {
        return unAccentedName;
    }

    public synchronized int isTransferPossible(Club buyer) { //returns 0 on success, 1 on already bought, 2 on insufficient budget
        if (!isTransferListed) return 1;
        if (buyer.getTransferBudget() < transferFee) return 2;
        return 0;
    }

    public void showDetails() {
        System.out.println("Name: " + name);
        System.out.println("Country: " + country);
        System.out.println("Age: " + age + " years");
        System.out.println("Height: " + height + "cm");
        System.out.println("Weight: " + weight + "kg");
        System.out.println("Club: " + clubName);
        System.out.println("Country: " + country);
        System.out.println("Position: " + position);
        System.out.println("Number: " + number);
        System.out.println("Preferred Foot: " + preferredFoot);
        System.out.println("Weekly Salary: " + Club.showCurrency(weeklySalary));
        System.out.println("Value: " + Club.showCurrency(estimatedValue));
    }
}
