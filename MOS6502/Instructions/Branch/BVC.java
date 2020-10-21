package Atari2600.MOS6502.Instructions.Branch;

import Atari2600.MOS6502.StatusRegister;

/**
 * Branch on overflow clear.
 */
public class BVC extends Branch {
    /**
     * Branch if V = 0
     */
    @Override
    boolean shouldBranch(StatusRegister sr) {
        return !sr.getFlag(6);
    }

    @Override
    public String toString() {
        return "BVC";
    }
}
