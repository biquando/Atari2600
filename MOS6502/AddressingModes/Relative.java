package Atari2600.MOS6502.AddressingModes;

import Atari2600.MOS6502.AddressingMode;
import Atari2600.MOS6502.CPU;

/**
 * Relative addressing (BCC, BCS, BNE, BEQ, BPL, BMI, BVC, BVS)
 *
 *  #   address  R/W description
 * --- --------- --- ---------------------------------------------
 *  1     PC      R  fetch opcode, increment PC
 *  2     PC      R  fetch operand, increment PC
 *  3     PC      R  Fetch opcode of next instruction,
 *                   If branch is taken, add operand to PCL.
 *                   Otherwise increment PC.
 *  4+    PC*     R  Fetch opcode of next instruction.
 *                   Fix PCH. If it did not change, increment PC.
 *  5!    PC      R  Fetch opcode of next instruction,
 *                   increment PC.
 *
 *  Notes: The opcode fetch of the next instruction is included to
 *         this diagram for illustration purposes. When determining
 *         real execution times, remember to subtract the last
 *         cycle.
 *
 *         * The high byte of Program Counter (PCH) may be invalid
 *         at this time, i.e. it may be smaller or bigger by $100.
 *
 *         + If branch is taken, this cycle will be executed.
 *
 *         ! If branch occurs to different page, this cycle will be
 *         executed.
 */
public class Relative implements AddressingMode {
    /**
     * 0: offset
     */
    @Override
    public byte[] fetch(CPU cpu) {
        // Cycle 2
        byte offset = cpu.mem.read(cpu.getPCL(), cpu.getPCH());
        cpu.incrementPC();
        return new byte[] { offset };
    }
}
