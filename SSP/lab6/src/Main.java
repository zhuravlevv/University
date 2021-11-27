public class Main {

    public static void main(String[] args) {

        RAM ram = new RAM("DDR4", "64GB");
        CPU cpu = new CPU("Intel core i5", 1D, 64);
        HardDisk hardDisk = new HardDisk("75 MB/sec", 256D, 32);
        GraphicsCard graphicsCard = new GraphicsCard("GeForce GTX 1050 Ti", "GP107", 1300);
        Motherboard motherboard = new Motherboard("lga 1150", cpu, ram, hardDisk, graphicsCard);
        Computer computer = new Computer(motherboard);
        System.out.println(computer);
    }
}
