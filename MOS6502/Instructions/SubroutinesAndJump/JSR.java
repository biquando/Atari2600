package Atari2600.MOS6502.Instructions.SubroutinesAndJump;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Jump to new location saving return address.
 */
public class JSR implements Instruction {
    /**
     * The address before the next instruction (PC - 1) is pushed onto the stack:
     * first the upper byte followed by the lower byte. As the stack grows backwards,
     * the return address is therefore stored as a little-endian number in memory.
     * PC is set to the target address.
     *
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
        return "JSR";
    }
}
