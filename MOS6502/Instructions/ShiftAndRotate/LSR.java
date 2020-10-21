package Atari2600.MOS6502.Instructions.ShiftAndRotate;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Logical shift right one bit (RMW).
 */
public class LSR implements Instruction {
    /**
     * 0 -> 7 6 5 4 3 2 1 0 -> C
     * Flags: N, Z, C
     * @param cpu processor that executes the instruction
     * @param data value, addr0, addr1
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        if (data.length == 0) {
            data = new byte[] { cpu.getA() };
        }

        cpu.sr.C(data[0] % 2 != 0); // Set flag C to bit 0 of data
        data[0] >>>= 1; // Logical shift right one bit
        cpu.sr.N(data[0]);
        cpu.sr.Z(data[0]);
        cpu.step();

        // Cycle 6
        if (data.length == 1) {
            cpu.setA(data[0]);
        } else {
            cpu.mem.write(data[0], data[1], data[2]);
        }
        cpu.end();
    }

    @Override
    public String toString() {
        return "LSR";
    }
}
