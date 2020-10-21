package Atari2600.MOS6502.Instructions.Branch;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;
import Atari2600.MOS6502.StatusRegister;

/**
 * Branch to a new place in memory if a condition is met.
 */
public abstract class Branch implements Instruction {
    /**
     * Evaluates shouldBranch(), and branches if true.
     * Takes 2 cycles if false, 3 cycles if true, and 4 cycles if
     * true and the branch is on a new page (PCH changes).
     *
     * @param cpu processor that executes the instruction
     * @param data offset
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        // Cycle 3?
        if (shouldBranch(cpu.sr)) {
            cpu.step();
            // Cycle 4?
            if (cpu.offsetPC(data[0])) {
                cpu.step();
            }
        } else {
//            cpu.setPCL((byte) (cpu.getPCL() - 1));
//            if (cpu.getPCL() == (byte)0xFF) {
//                cpu.setPCH((byte) (cpu.getPCH() - 1));
//            }
        }
        cpu.end();
    }

    /**
     * Should be called in execute() to determine whether the branch is taken.
     * @param sr flags to evaluate (cpu.sr)
     * @return whether the branch should be taken
     */
    abstract boolean shouldBranch(StatusRegister sr);
}
