public class Motherboard {

    private String socket;
    private CPU cpu;
    private RAM ram;
    private HardDisk hardDisk;
    private GraphicsCard graphicsCard;

    public Motherboard() {
    }

    public Motherboard(String socket, CPU cpu, RAM ram, HardDisk hardDisk, GraphicsCard graphicsCard) {
        this.socket = socket;
        this.cpu = cpu;
        this.ram = ram;
        this.hardDisk = hardDisk;
        this.graphicsCard = graphicsCard;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public RAM getRam() {
        return ram;
    }

    public void setRam(RAM ram) {
        this.ram = ram;
    }

    public HardDisk getHardDisk() {
        return hardDisk;
    }

    public void setHardDisk(HardDisk hardDisk) {
        this.hardDisk = hardDisk;
    }

    public GraphicsCard getGraphicsCard() {
        return graphicsCard;
    }

    public void setGraphicsCard(GraphicsCard graphicsCard) {
        this.graphicsCard = graphicsCard;
    }

    @Override
    public String toString() {
        return "Motherboard{" +
                "socket='" + socket + '\'' +
                ", cpu=" + cpu +
                ", ram=" + ram +
                ", hardDisk=" + hardDisk +
                ", graphicsCard=" + graphicsCard +
                '}';
    }
}
