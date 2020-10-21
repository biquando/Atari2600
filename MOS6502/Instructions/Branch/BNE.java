package Atari2600.MOS6502.Instructions.Branch;

import Atari2600.MOS6502.StatusRegister;

/**
 * Branch on result not zero.
 */
public class BNE extends Branch {
    /**
     * Branch if Z = 0
     */
    @Override
    boolean shouldBranch(StatusRegister sr) {
        return !sr.getFlag(1);
    }

    @Override
    public String toString() {
        return "BNE";
    }
}
