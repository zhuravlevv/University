import org.nevec.rjm.BigDecimalMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Main2 implements MainInterface{

    public static final BigInteger N1 = new BigInteger("389091381643");
    public static final BigInteger N2 = new BigInteger("391569053221");
    public static final BigInteger N3 = new BigInteger("393864798289");

    public static final BigInteger C1 = new BigInteger("23283117034" +
            "199910300344" +
            "122379231308" +
            "129836433029" +
            "266167362913" +
            "322794903721" +
            "164367877138" +
            "317459368677" +
            "210705957227" +
            "38878534867" +
            "199295177267" +
            "116980227366");
    public static final BigInteger C2 = new BigInteger("81950696329" +
            "310054893565" +
            "132301878314" +
            "52795246284" +
            "276197768422" +
            "265696804182" +
            "238369333190" +
            "66855113681" +
            "316766995365" +
            "321182915473" +
            "118193576787" +
            "190068391425");

    public static final BigInteger C3 = new BigInteger("57844537762" +
            "368640254231" +
            "220965124671" +
            "260183659429" +
            "299904567942" +
            "286935730637" +
            "266053541214" +
            "146542714390" +
            "79442443012" +
            "28368938795" +
            "30970811879" +
            "72570776324");

    private static final int e = 3;

    private static BigInteger M0;

    private static BigInteger m1;
    private static BigInteger m2;
    private static BigInteger m3;

    private static BigInteger n1;
    private static BigInteger n2;
    private static BigInteger n3;

    private static BigInteger C1n1m1;
    private static BigInteger C2n2m2;
    private static BigInteger C3n3m3;

    private static BigInteger S;
    private static BigInteger SModM0;

    private static BigInteger M;

    public static void main(String[] args) {

        M0 = N1.multiply(N2).multiply(N3);
        System.out.println("M0 = " + M0);

        m1 = N2.multiply(N3);
        m2 = N1.multiply(N3);
        m3 = N1.multiply(N2);
        System.out.println("m1 = " + m1);
        System.out.println("m2 = " + m2);
        System.out.println("m3 = " + m3);

        n1 = m1.modInverse(N1);
        n2 = m2.modInverse(N2);
        n3 = m3.modInverse(N3);
        System.out.println("n1 = " + n1);
        System.out.println("n2 = " + n2);
        System.out.println("n3 = " + n3);

        C1n1m1 = C1.multiply(n1).multiply(m1);
        C2n2m2 = C2.multiply(n2).multiply(m2);
        C3n3m3 = C3.multiply(n3).multiply(m3);

        S = C1n1m1.add(C2n2m2).add(C3n3m3);
        System.out.println("S = " + S);

        SModM0 = S.mod(M0);
        System.out.println("SModM0 = " + SModM0);

        BigDecimal Md = new BigDecimal(SModM0);
        BigDecimal ed = BigDecimal.ONE.divide(BigDecimal.valueOf(e), MathContext.DECIMAL128);
        System.out.println("ed = " + ed);
        M = BigDecimalMath.pow(Md, ed).toBigInteger();
        System.out.println("M = " + M);
    }

    @Override
    public void start() {

        M0 = N1.multiply(N2).multiply(N3);
        System.out.println("M0 = " + M0);

        m1 = N2.multiply(N3);
        m2 = N1.multiply(N3);
        m3 = N1.multiply(N2);
        System.out.println("m1 = " + m1);
        System.out.println("m2 = " + m2);
        System.out.println("m3 = " + m3);

        n1 = m1.modInverse(N1);
        n2 = m2.modInverse(N2);
        n3 = m3.modInverse(N3);
        System.out.println("n1 = " + n1);
        System.out.println("n2 = " + n2);
        System.out.println("n3 = " + n3);

        C1n1m1 = C1.multiply(n1).multiply(m1);
        C2n2m2 = C2.multiply(n2).multiply(m2);
        C3n3m3 = C3.multiply(n3).multiply(m3);

        S = C1n1m1.add(C2n2m2).add(C3n3m3);
        System.out.println("S = " + S);

        SModM0 = S.mod(M0);
        System.out.println("SModM0 = " + SModM0);

        BigDecimal Md = new BigDecimal(SModM0);
        BigDecimal ed = BigDecimal.ONE.divide(BigDecimal.valueOf(e), MathContext.DECIMAL128);
        System.out.println("ed = " + ed);
        M = BigDecimalMath.pow(Md, ed).toBigInteger();
        System.out.println("M = " + M);
    }

    @Override
    public BigInteger getN1() {
        return N1;
    }

    @Override
    public BigInteger getN2() {
        return N2;
    }

    @Override
    public BigInteger getN3() {
        return N3;
    }

    @Override
    public BigInteger getC1() {
        return C1;
    }

    @Override
    public BigInteger getC2() {
        return C2;
    }

    @Override
    public BigInteger getC3() {
        return C3;
    }

    @Override
    public BigInteger getM0() {
        return M0;
    }

    @Override
    public BigInteger getm1() {
        return m1;
    }

    @Override
    public BigInteger getm2() {
        return m2;
    }

    @Override
    public BigInteger getm3() {
        return m3;
    }

    @Override
    public BigInteger getn1() {
        return n1;
    }

    @Override
    public BigInteger getn2() {
        return n2;
    }

    @Override
    public BigInteger getn3() {
        return n3;
    }

    @Override
    public BigInteger getC1n1m1() {
        return C1n1m1;
    }

    @Override
    public BigInteger getC2n2m2() {
        return C2n2m2;
    }

    @Override
    public BigInteger getC3n3m3() {
        return C3n3m3;
    }

    @Override
    public BigInteger getS() {
        return S;
    }

    @Override
    public BigInteger getSModM0() {
        return SModM0;
    }

    @Override
    public BigInteger getM() {
        return M;
    }

    @Override
    public int getE() {
        return e;
    }
}
