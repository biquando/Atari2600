package Atari2600.MOS6502.Instructions.SubroutinesAndJump;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Jump to new location.
 */
public class JMP implements Instruction {
    /**
     * Jump to new location by changing the value of the program counter.
     * @param cpu processor that executes the instruction
     * @param data addr0, addr1
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setPCL(data[0]);
        cpu.setPCH(data[1]);
        cpu.end();
    }

    @Override
    public String toString() {
        return "JMP";
    }
}
