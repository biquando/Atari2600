package Atari2600.MOS6502;

/**
 * Stores the processor status flags and provides methods to
 * easily manipulate them.
 *
 * 7 - N - Negative
 * 6 - V - Overflow
 * 5 -   - (Unused)
 * 4 - B - Break
 * 3 - D - Decimal
 * 2 - I - Interrupt Disable
 * 1 - Z - Zero
 * 0 - C - Carry
 */
public class StatusRegister {
    private byte sr;

    public StatusRegister() {
        sr = 0;
    }

    /**
     * Set N if the data is negative, i.e. set to bit 7 of data
     */
    public void N(byte data) {
        sr &= 0b01111111;
        sr |= data & 0b10000000;
    }

    /**
     * Set to bit 6 of data.
     *
     * Arithmetic: Set if a signed overflow occurred during addition
     * or subtraction,i.e. the sign of the result differs from the sign
     * of both the input and the accumulator.
     *
     * BIT: Set to bit 6 of the input.
     */
    public void V(byte data) {
        sr &= 0b10111111;
        sr |= data & 0b0100000;
    }

    /**
     * Set if result was zero
     */
    public void Z(byte data) {
        if (data == 0) {
            sr |= 0b00000010;
        } else {
            sr &= 0b11111101;
        }
    }

    /**
     * Carry/Borrow flag used in math and rotate operations
     *
     * Arithmetic: Set if an unsigned overflow occurred during addition
     * or subtraction, i.e. the result is less than the initial value
     *
     * Compare: Set if register's value is greater than or equal to the
     * input value Shifting: Set to the value of the eliminated bit of the
     * input, i.e. bit 7 when shifting left, or bit 0 when shifting right
     */
    public void C(boolean value) {
        setFlag(0, value);
    }

    /**
     * Returns true if the flag at idx is set.
     * See class javadoc for indices.
     */
    public boolean getFlag(int idx) {
        return (((int)sr & 0xff) >> idx) % 2 == 1;
    }

    /**
     * Set any flag to a value.
     */
    public void setFlag(int idx, boolean value) {
        if (value) { sr |= 1 << idx; }
        else { sr &= (byte)0xff - ((byte)1 << idx); }
    }

    public byte toByte() {
        return sr;
    }

    public void set(byte b) {
        sr = b;
    }
}
