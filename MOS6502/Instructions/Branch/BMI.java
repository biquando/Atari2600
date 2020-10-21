package Atari2600.MOS6502.Instructions.Branch;

import Atari2600.MOS6502.StatusRegister;

/**
 * Branch on result minus.
 */
public class BMI extends Branch {
    /**
     * Branch if N = 1
     */
    @Override
    boolean shouldBranch(StatusRegister sr) {
        return sr.getFlag(7);
    }

    @Override
    public String toString() {
        return "BMI";
    }
}
