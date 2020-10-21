package Atari2600.MOS6502.Instructions.Logic;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Exclusive-OR memory with accumulator (read).
 */
public class EOR implements Instruction {
    /**
     * A ^ M -> A
     * Flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data value
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        data[0] = (byte)(cpu.getA() ^ data[0]);
        cpu.setA(data[0]);
        cpu.sr.N(data[0]);
        cpu.sr.Z(data[0]);
        cpu.end();
    }

    @Override
    public String toString() {
        return "EOR";
    }
}
