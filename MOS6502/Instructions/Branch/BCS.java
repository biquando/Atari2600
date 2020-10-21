package Atari2600.MOS6502.Instructions.Branch;

import Atari2600.MOS6502.StatusRegister;

/**
 * Branch on carry set.
 */
public class BCS extends Branch {
    /**
     * Branch if C = 1
     */
    @Override
    boolean shouldBranch(StatusRegister sr) {
        return sr.getFlag(0);
    }

    @Override
    public String toString() {
        return "BCS";
    }
}
