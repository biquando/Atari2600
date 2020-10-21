package Atari2600.MOS6502.AddressingModes.Absolute;

import Atari2600.MOS6502.CPU;

/**
 * Absolute indirect addressing (JMP)
 *
 *  #   address  R/W description
 * --- --------- --- ------------------------------------------
 *  1     PC      R  fetch opcode, increment PC
 *  2     PC      R  fetch pointer address low, increment PC
 *  3     PC      R  fetch pointer address high, increment PC
 *  4   pointer   R  fetch low address to latch
 *  5  pointer+1* R  fetch PCH, copy latch to PCL
 *
 *  Note: * The PCH will always be fetched from the same page
 *  than PCL, i.e. page boundary crossing is not handled.
 */
public class AbsoluteIndirect extends Absolute {
    /**
     * 0: addr0
     * 1: addr1
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte[] data = super.fetch(cpu);
        cpu.step();

        // Cycle 4
        byte addr0 = cpu.mem.read(data[0], data[1]);
        cpu.step();

        // Cycle 5
        byte addr1 = cpu.mem.read((byte)(data[0] + 1), data[1]);
        return new byte[] { addr0, addr1 };
    }
}
