package Atari2600.MOS6502.AddressingModes.Absolute;

import Atari2600.MOS6502.AddressingMode;
import Atari2600.MOS6502.CPU;

/**
 * JSR
 *
 *  #  address R/W description
 * --- ------- --- -------------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  fetch low address byte, increment PC
 *  3  $0100,S  R  internal operation (predecrement S?)
 *  4  $0100,S  W  push PCH on stack, decrement S
 *  5  $0100,S  W  push PCL on stack, decrement S
 *  6    PC     R  copy low address byte to PCL, fetch high address
 *                 byte to PCH
 */
public class AbsoluteJSR implements AddressingMode {
    /**
     * 0: addr0
     * 1: addr1
     */
    @Override
    public byte[] fetch(CPU cpu) {
        // Cycle 2
        byte addr0 = cpu.mem.read(cpu.getPCL(), cpu.getPCH());
        cpu.incrementPC();
        cpu.step();

        // Cycle 3
        // Internal operation (predecrement S?)
        cpu.step();

        // Cycle 4
        cpu.mem.write(cpu.getPCH(), cpu.getSP(), (byte)0x01);
        cpu.setSP((byte)(cpu.getSP() - 1));
        cpu.step();

        // Cycle 5
        cpu.mem.write(cpu.getPCL(), cpu.getSP(), (byte)0x01);
        cpu.setSP((byte)(cpu.getSP() - 1));
        cpu.step();

        // Cycle 6
        byte addr1 = cpu.mem.read(cpu.getPCL(), cpu.getPCH());
        return new byte[] { addr0, addr1 };
    }
}
