package Atari2600.MOS6502;

/**
 * Template class for cpu addressing modes.
 * Opcode is fetched and PC is incremented BEFORE the instruction is performed.
 */
public interface AddressingMode {
    /**
     * Fetches the instruction's operand.
     *
     * Cycle 1 is the same for every instruction (fetch code, increment PC),
     * so it should be performed beforehand in order to determine which
     * instruction will be executed. The final cycle(s) should be
     * performed by the instruction that receives this data.
     *
     * @param cpu processor to do the instruction on
     * @return some value or address that the instruction should use
     */
    byte[] fetch(CPU cpu);
}
