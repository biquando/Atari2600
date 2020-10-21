package Atari2600.MOS6502.Instructions.Arithmetic;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Add memory to accumulator with carry (read).
 */
public class ADC implements Instruction {
    /**
     * A + M + C -> A
     * Flags: N, V, Z, C
     * @param cpu processor that executes the instruction
     * @param data value
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        int sum = ((int)cpu.getA() & 0xff) + ((int)data[0] & 0xff); // A + M
        if (cpu.sr.getFlag(0)) { sum++; } // + C

        byte c = (byte)(sum & 0xff);
        cpu.sr.N(c); // N flag
        cpu.sr.setFlag(6, ((cpu.getA() ^ sum) & (data[0] ^ sum) & 0x80) != 0); // V flag
        cpu.sr.Z(c); // Z flag
        cpu.sr.setFlag(0, sum > 0xff); // C flag

        cpu.setA(c); // Put sum in accumulator
        cpu.end();
    }

    @Override
    public String toString() {
        return "ADC";
    }
}
