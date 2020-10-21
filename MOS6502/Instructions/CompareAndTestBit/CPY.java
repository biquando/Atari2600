package Atari2600.MOS6502.Instructions.CompareAndTestBit;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Compare memory and Y (read).
 */
public class CPY implements Instruction {
    /**
     * Y - M
     * Flags: N, Z, C
     * @param cpu processor that executes the instruction
     * @param data value
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        data[0] = (byte)(256 - data[0]);
        int sum = ((int)cpu.getY() & 0xff) + ((int)data[0] & 0xff); // Y + -M

        byte c = (byte)(sum & 0xff);
        cpu.sr.N(c); // N flag
        cpu.sr.Z(c); // Z flag
        cpu.sr.setFlag(0, sum > 0xff); // C flag

        cpu.end();
    }

    @Override
    public String toString() {
        return "CPY";
    }
}
