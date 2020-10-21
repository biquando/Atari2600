package Atari2600.MOS6502;

/**
 * Template class for the end of a cpu instruction.
 */
public interface Instruction {
    /**
     * Executes this instruction.
     *
     * This method should always end with cpu.end() to finish the last
     * cycle, instead of cpu.step(). This tells the cpu to proceed with
     * the next instruction at PC.
     *
     * @param cpu processor that executes the instruction
     * @param data value or address from the addressing mode
     */
    void execute(CPU cpu, byte[] data);
}
