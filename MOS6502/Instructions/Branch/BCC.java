package Atari2600.MOS6502.Instructions.Branch;

import Atari2600.MOS6502.StatusRegister;

/**
 * Branch on carry clear.
 */
public class BCC extends Branch {
    /**
     * Branch if C = 0
     */
    @Override
    boolean shouldBranch(StatusRegister sr) {
        return !sr.getFlag(0);
    }

    @Override
    public String toString() {
        return "BCC";
    }
}
