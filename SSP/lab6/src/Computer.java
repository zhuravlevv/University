public class Computer {

    private Motherboard motherboard;

    public Computer() {
    }

    public Computer(Motherboard motherboard) {
        this.motherboard = motherboard;
    }

    public Motherboard getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(Motherboard motherboard) {
        this.motherboard = motherboard;
    }

    @Override
    public String toString() {
        return "Computer{\n" +
                "\tmotherboard=" + motherboard +
                "\n}";
    }
}
