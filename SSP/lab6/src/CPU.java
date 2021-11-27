public class CPU {
    private String model;
    private Double clockFrequency;
    private Integer bitness;

    public CPU() {
    }

    public CPU(String model, Double clockFrequency, Integer bitness) {
        this.model = model;
        this.clockFrequency = clockFrequency;
        this.bitness = bitness;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getClockFrequency() {
        return clockFrequency;
    }

    public void setClockFrequency(Double clockFrequency) {
        this.clockFrequency = clockFrequency;
    }

    public Integer getBitness() {
        return bitness;
    }

    public void setBitness(Integer bitness) {
        this.bitness = bitness;
    }

    @Override
    public String toString() {
        return "CPU{" +
                "model='" + model + '\'' +
                ", clockFrequency=" + clockFrequency +
                ", bitness=" + bitness +
                '}';
    }
}
