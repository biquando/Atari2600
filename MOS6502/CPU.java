package Atari2600.MOS6502;

import Atari2600.MOS6502.AddressingModes.*;
import Atari2600.MOS6502.AddressingModes.Absolute.*;
import Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteX.*;
import Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteY.*;
import Atari2600.MOS6502.AddressingModes.ZeroPage.*;
import Atari2600.MOS6502.AddressingModes.ZeroPage.IndexedIndirect.*;
import Atari2600.MOS6502.AddressingModes.ZeroPage.IndirectIndexed.*;
import Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed.ZeroPageX.*;
import Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed.ZeroPageY.*;

import Atari2600.MOS6502.Instructions.Arithmetic.*;
import Atari2600.MOS6502.Instructions.Branch.*;
import Atari2600.MOS6502.Instructions.CompareAndTestBit.*;
import Atari2600.MOS6502.Instructions.IncrementAndDecrement.*;
import Atari2600.MOS6502.Instructions.LoadAndStore.*;
import Atari2600.MOS6502.Instructions.Logic.*;
import Atari2600.MOS6502.Instructions.Miscellaneous.*;
import Atari2600.MOS6502.Instructions.SetAndClear.*;
import Atari2600.MOS6502.Instructions.ShiftAndRotate.*;
import Atari2600.MOS6502.Instructions.Stack.*;
import Atari2600.MOS6502.Instructions.SubroutinesAndJump.*;
import Atari2600.MOS6502.Instructions.Transfer.*;

import Atari2600.Memory.Memory;

public class CPU implements Runnable {

    /**
     * a: accumulator
     * x: index X
     * y: index Y
     * sp: stack pointer
     * pcl: low byte of program counter
     * pch: high byte of program counter
     */
    private byte a, x, y, sp, pcl, pch;

    /**
     * The memory component that the cpu should talk to when performing
     * all read/write instructions.
     */
    public Memory mem;

    /**
     * Stores the processor status flags and provides methods to
     * easily manipulate them.
     */
    public StatusRegister sr;

    /**
     * Each opcode (idx) is associated with an addressing mode and an instruction.
     * When execute() is called, the byte at PC is read and used as an index for
     * these two arrays to find the corresponding mode/instruction.
     */
    private AddressingMode[] addressingModes;
    private Instruction[] instructions;

    private long lastCycle, cycleDelay, cycleNum;
    private volatile boolean showDebug, running, ready;

    public CPU(Memory mem) {
        sp = (byte)0xfd;
        pch = (byte)0xf0;
        sr = new StatusRegister();

        addressingModes = createAddressingModes();
        instructions = createInstructions();

        cycleDelay = 3000;
        cycleNum = 0;

        this.mem = mem;
        mem.tia.use(this);
        Thread t = new Thread(mem.tia, "tia-thread");
        t.start();
    }

    // Cycles ------------------------------------------------------------------

    /**
     * Moves to the next cpu cycle.
     */
    public void step() {
        mem.tia.sync();
        if (showDebug) { System.out.println(this); }
        cycleNum++;
        lastCycle += cycleDelay;
        long nextCycle;
        do {
            nextCycle = System.nanoTime();
        } while (nextCycle < lastCycle || !running || mem.tia.isReady());
        if (nextCycle - lastCycle >= cycleDelay * 2) {
            lastCycle = nextCycle;
        }
    }

    /**
     * Signifies the end of an instruction.
     * This automatically moves to the next cycle, so use in place of step().
     */
    public void end() {
        step();
        ready = true;
    }

    /**
     * Executes the next instruction located at PC.
     */
    public void execute() {
        // Cycle 1
        int opcode = (int)mem.read(pcl, pch) & 0xff;
        incrementPC();
        if (showDebug) {
            System.out.println(instructions[opcode]);
        }
        step();

        // Cycle 2
        instructions[opcode].execute(this, addressingModes[opcode].fetch(this));
    }

    // Start and stop ----------------------------------------------------------

    @Override
    public void run() {
        restart();
        while (true) {
            if (running && ready) {
                ready = false;
                execute();
            }
        }
    }

    /**
     * Halts the processor until restart() is called.
     */
    public void stop() {
        running = false;
    }

    /**
     * Starts the processor again if it was halted with stop().
     */
    public void restart() {
        running = true;
        ready = true;
        lastCycle = System.nanoTime();
    }

    // Manipulate program counter ----------------------------------------------

    /**
     * Moves the program counter forward.
     */
    public void incrementPC() {
        if (pcl++ == (byte)0xff) {
            pch++;
        }
    }

    /**
     * Offset the program counter (used in relative addressing).
     * @param offset ranges from -128 to 127
     * @return if the new address is on a different page (pch changes)
     */
    public boolean offsetPC(byte offset) {
        byte newPcl = (byte)(pcl + offset);
        boolean pageChange = false;
        if (offset < 0 && pcl > 0 && newPcl < 0) {
            pch--; // underflow
            pageChange = true;
        } else if (offset > 0 && pcl < 0 && newPcl > 0) {
            pch++; // overflow
            pageChange = true;
        }
        pcl = newPcl;
        return pageChange;
    }

    // -------------------------------------------------------------------------

    private AddressingMode[] createAddressingModes() {
        AddressingMode[] arr = new AddressingMode[256];

        AddressingMode imp = new Implied();
        AddressingMode imm = new Immediate();
        AddressingMode rel = new Relative();

        AddressingMode abs = new Absolute();
        AddressingMode absR = new Absolute_Read();
        AddressingMode absM = new Absolute_RMW();
        AddressingMode absW = new Absolute_Write();
        AddressingMode indirect = new AbsoluteIndirect();
        AddressingMode absJSR = new AbsoluteJSR();

        AddressingMode absxR = new AbsoluteX_Read();
        AddressingMode absxM = new AbsoluteX_RMW();
        AddressingMode absxW = new AbsoluteX_Write();
        AddressingMode absyR = new AbsoluteY_Read();
        AddressingMode absyW = new AbsoluteY_Write();

        AddressingMode zpgR = new ZeroPage_Read();
        AddressingMode zpgM = new ZeroPage_RMW();
        AddressingMode zpgW = new ZeroPage_Write();

        AddressingMode zpgxR = new ZeroPageX_Read();
        AddressingMode zpgxM = new ZeroPageX_RMW();
        AddressingMode zpgxW = new ZeroPageX_Write();
        AddressingMode zpgyR = new ZeroPageY_Read();
        AddressingMode zpgyW = new ZeroPageY_Write();

        AddressingMode idxindR = new IndexedIndirect_Read();
        AddressingMode idxindW = new IndexedIndirect_Write();
        AddressingMode indidxR = new IndirectIndexed_Read();
        AddressingMode indidxW = new IndirectIndexed_Write();

        arr[0x0] = imp;
        arr[0x1] = idxindR;
        arr[0x5] = zpgR;
        arr[0x6] = zpgM;
        arr[0x8] = imp;
        arr[0x9] = imm;
        arr[0xa] = imp;
        arr[0xd] = absR;
        arr[0xe] = absM;

        arr[0x10] = rel;
        arr[0x11] = indidxR;
        arr[0x15] = zpgxR;
        arr[0x16] = zpgxM;
        arr[0x18] = imp;
        arr[0x19] = absyR;
        arr[0x1d] = absxR;
        arr[0x1e] = absxM;

        arr[0x20] = absJSR;
        arr[0x21] = idxindR;
        arr[0x24] = zpgR;
        arr[0x25] = zpgR;
        arr[0x26] = zpgM;
        arr[0x28] = imp;
        arr[0x29] = imm;
        arr[0x2a] = imp;
        arr[0x2c] = absR;
        arr[0x2d] = absR;
        arr[0x2e] = absM;

        arr[0x30] = rel;
        arr[0x31] = indidxR;
        arr[0x35] = zpgxR;
        arr[0x36] = zpgxM;
        arr[0x38] = imp;
        arr[0x39] = absyR;
        arr[0x3d] = absxR;
        arr[0x3e] = absxM;

        arr[0x40] = imp;
        arr[0x41] = idxindR;
        arr[0x45] = zpgR;
        arr[0x46] = zpgM;
        arr[0x48] = imp;
        arr[0x49] = imm;
        arr[0x4a] = imp;
        arr[0x4c] = abs;
        arr[0x4d] = absR;
        arr[0x4e] = absM;

        arr[0x50] = rel;
        arr[0x51] = indidxR;
        arr[0x55] = zpgxR;
        arr[0x56] = zpgxM;
        arr[0x58] = imp;
        arr[0x59] = absyR;
        arr[0x5d] = absxR;
        arr[0x5e] = absM;

        arr[0x60] = imp;
        arr[0x61] = idxindR;
        arr[0x65] = zpgR;
        arr[0x66] = zpgM;
        arr[0x68] = imp;
        arr[0x69] = imm;
        arr[0x6a] = imp;
        arr[0x6c] = indirect;
        arr[0x6d] = absR;
        arr[0x6e] = absM;

        arr[0x70] = rel;
        arr[0x71] = indidxR;
        arr[0x75] = zpgxR;
        arr[0x76] = zpgxM;
        arr[0x78] = imp;
        arr[0x79] = absyR;
        arr[0x7d] = absxR;
        arr[0x7e] = absxM;

        arr[0x81] = idxindW;
        arr[0x84] = zpgW;
        arr[0x85] = zpgW;
        arr[0x86] = zpgW;
        arr[0x88] = imp;
        arr[0x8a] = imp;
        arr[0x8c] = absW;
        arr[0x8d] = absW;
        arr[0x8e] = absW;

        arr[0x90] = rel;
        arr[0x91] = indidxW;
        arr[0x94] = zpgxW;
        arr[0x95] = zpgxW;
        arr[0x96] = zpgyW;
        arr[0x98] = imp;
        arr[0x99] = absyW;
        arr[0x9a] = imp;
        arr[0x9d] = absxW;

        arr[0xa0] = imm;
        arr[0xa1] = idxindR;
        arr[0xa2] = imm;
        arr[0xa4] = zpgR;
        arr[0xa5] = zpgR;
        arr[0xa6] = zpgR;
        arr[0xa8] = imp;
        arr[0xa9] = imm;
        arr[0xaa] = imp;
        arr[0xac] = absR;
        arr[0xad] = absR;
        arr[0xae] = absR;

        arr[0xb0] = rel;
        arr[0xb1] = indidxR;
        arr[0xb4] = zpgxR;
        arr[0xb5] = zpgxR;
        arr[0xb6] = zpgyR;
        arr[0xb8] = imp;
        arr[0xb9] = absyR;
        arr[0xba] = imp;
        arr[0xbc] = absR;
        arr[0xbd] = absR;
        arr[0xbe] = absyR;

        arr[0xc0] = imm;
        arr[0xc1] = idxindR;
        arr[0xc4] = zpgR;
        arr[0xc5] = zpgR;
        arr[0xc6] = zpgM;
        arr[0xc8] = imp;
        arr[0xc9] = imm;
        arr[0xca] = imp;
        arr[0xcc] = absR;
        arr[0xcd] = absR;
        arr[0xce] = absM;

        arr[0xd0] = rel;
        arr[0xd1] = indidxR;
        arr[0xd5] = zpgxR;
        arr[0xd6] = zpgxM;
        arr[0xd8] = imp;
        arr[0xd9] = absyR;
        arr[0xdd] = absxR;
        arr[0xde] = absxM;

        arr[0xe0] = imm;
        arr[0xe1] = idxindR;
        arr[0xe4] = zpgR;
        arr[0xe5] = zpgR;
        arr[0xe6] = zpgM;
        arr[0xe8] = imp;
        arr[0xe9] = imm;
        arr[0xea] = imp;
        arr[0xec] = absR;
        arr[0xed] = absR;
        arr[0xee] = absM;

        arr[0xf0] = rel;
        arr[0xf1] = indidxR;
        arr[0xf5] = zpgxR;
        arr[0xf6] = zpgxM;
        arr[0xf8] = imp;
        arr[0xf9] = absyR;
        arr[0xfd] = absxR;
        arr[0xfe] = absxM;

        return arr;
    }

    private Instruction[] createInstructions() {
        Instruction[] arr = new Instruction[256];

        arr[0x0] = new BRK();
        arr[0x1] = new ORA();
        arr[0x5] = arr[0x1];
        arr[0x6] = new ASL();
        arr[0x8] = new PHP();
        arr[0x9] = arr[0x1];
        arr[0xa] = arr[0x6];
        arr[0xd] = arr[0x1];
        arr[0xe] = arr[0x6];

        arr[0x10] = new BPL();
        arr[0x11] = arr[0x1];
        arr[0x15] = arr[0x1];
        arr[0x16] = arr[0x6];
        arr[0x18] = new CLC();
        arr[0x19] = arr[0x1];
        arr[0x1d] = arr[0x1];
        arr[0x1e] = arr[0x6];

        arr[0x20] = new JSR();
        arr[0x21] = new AND();
        arr[0x24] = new BIT();
        arr[0x25] = arr[0x21];
        arr[0x26] = new ROL();
        arr[0x28] = new PLP();
        arr[0x29] = arr[0x21];
        arr[0x2a] = arr[0x26];
        arr[0x2c] = arr[0x24];
        arr[0x2d] = arr[0x21];
        arr[0x2e] = arr[0x26];

        arr[0x30] = new BMI();
        arr[0x31] = arr[0x21];
        arr[0x35] = arr[0x21];
        arr[0x36] = arr[0x26];
        arr[0x38] = new SEC();
        arr[0x39] = arr[0x21];
        arr[0x3d] = arr[0x21];
        arr[0x3e] = arr[0x26];

        arr[0x40] = new RTI();
        arr[0x41] = new EOR();
        arr[0x45] = arr[0x41];
        arr[0x46] = new LSR();
        arr[0x48] = new PHA();
        arr[0x49] = arr[0x41];
        arr[0x4a] = arr[0x46];
        arr[0x4c] = new JMP();
        arr[0x4d] = arr[0x41];
        arr[0x4e] = arr[0x46];

        arr[0x50] = new BVC();
        arr[0x51] = arr[0x41];
        arr[0x55] = arr[0x41];
        arr[0x56] = arr[0x46];
        arr[0x58] = new CLI();
        arr[0x59] = arr[0x41];
        arr[0x5d] = arr[0x41];
        arr[0x5e] = arr[0x46];

        arr[0x60] = new RTS();
        arr[0x61] = new ADC();
        arr[0x65] = arr[0x61];
        arr[0x66] = new ROR();
        arr[0x68] = new PLA();
        arr[0x69] = arr[0x61];
        arr[0x6a] = arr[0x66];
        arr[0x6c] = arr[0x4c];
        arr[0x6d] = arr[0x61];
        arr[0x6e] = arr[0x66];

        arr[0x70] = new BVS();
        arr[0x71] = arr[0x61];
        arr[0x75] = arr[0x61];
        arr[0x76] = arr[0x66];
        arr[0x78] = new SEI();
        arr[0x79] = arr[0x61];
        arr[0x7d] = arr[0x61];
        arr[0x7e] = arr[0x66];

        arr[0x81] = new STA();
        arr[0x84] = new STY();
        arr[0x85] = arr[0x81];
        arr[0x86] = new STX();
        arr[0x88] = new DEY();
        arr[0x8a] = new TXA();
        arr[0x8c] = arr[0x84];
        arr[0x8d] = arr[0x81];
        arr[0x8e] = arr[0x86];

        arr[0x90] = new BCC();
        arr[0x91] = arr[0x81];
        arr[0x94] = arr[0x84];
        arr[0x95] = arr[0x81];
        arr[0x96] = arr[0x86];
        arr[0x98] = new TYA();
        arr[0x99] = arr[0x81];
        arr[0x9a] = new TXS();
        arr[0x9d] = arr[0x81];

        arr[0xa0] = new LDY();
        arr[0xa1] = new LDA();
        arr[0xa2] = new LDX();
        arr[0xa4] = arr[0xa0];
        arr[0xa5] = arr[0xa1];
        arr[0xa6] = arr[0xa2];
        arr[0xa8] = new TAY();
        arr[0xa9] = arr[0xa1];
        arr[0xaa] = new TAX();
        arr[0xac] = arr[0xa0];
        arr[0xad] = arr[0xa1];
        arr[0xae] = arr[0xa2];

        arr[0xb0] = new BCS();
        arr[0xb1] = arr[0xa1];
        arr[0xb4] = arr[0xa0];
        arr[0xb5] = arr[0xa1];
        arr[0xb6] = arr[0xa2];
        arr[0xb8] = new CLV();
        arr[0xb9] = arr[0xa1];
        arr[0xba] = new TSX();
        arr[0xbc] = arr[0xa0];
        arr[0xbd] = arr[0xa1];
        arr[0xbe] = arr[0xa2];

        arr[0xc0] = new CPY();
        arr[0xc1] = new CMP();
        arr[0xc4] = arr[0xc0];
        arr[0xc5] = arr[0xc1];
        arr[0xc6] = new DEC();
        arr[0xc8] = new INY();
        arr[0xc9] = arr[0xc1];
        arr[0xca] = new DEX();
        arr[0xcc] = arr[0xc0];
        arr[0xcd] = arr[0xc1];
        arr[0xce] = arr[0xc6];

        arr[0xd0] = new BNE();
        arr[0xd1] = arr[0xc1];
        arr[0xd5] = arr[0xc1];
        arr[0xd6] = arr[0xc6];
        arr[0xd8] = new CLD();
        arr[0xd9] = arr[0xc1];
        arr[0xdd] = arr[0xc1];
        arr[0xde] = arr[0xc6];

        arr[0xe0] = new CPX();
        arr[0xe1] = new SBC();
        arr[0xe4] = arr[0xe0];
        arr[0xe5] = arr[0xe1];
        arr[0xe6] = new INC();
        arr[0xe8] = new INX();
        arr[0xe9] = arr[0xe1];
        arr[0xea] = new NOP();
        arr[0xec] = arr[0xe0];
        arr[0xed] = arr[0xe1];
        arr[0xee] = arr[0xe6];

        arr[0xf0] = new BEQ();
        arr[0xf1] = arr[0xe1];
        arr[0xf5] = arr[0xe1];
        arr[0xf6] = arr[0xe6];
        arr[0xf8] = new SED();
        arr[0xf9] = arr[0xe1];
        arr[0xfd] = arr[0xe1];
        arr[0xfe] = arr[0xe6];

        return arr;
    }

    @Override
    public String toString() {
        return "cycleNum: " + cycleNum +
             "\na: " + a + ", x: " + x + ", y: " + y +
             "\npcl: " + Integer.toHexString(Byte.toUnsignedInt(pcl)) + ", pch: " +
                Integer.toHexString(Byte.toUnsignedInt(pch)) +
             "\nsr: " + Integer.toBinaryString(Byte.toUnsignedInt(sr.toByte())) +
             "\nlastCycle: " + lastCycle + "\n";
    }

    public byte getA() {
        return a;
    }

    public void setA(byte a) {
        this.a = a;
    }

    public byte getX() {
        return x;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public byte getY() {
        return y;
    }

    public void setY(byte y) {
        this.y = y;
    }

    public byte getSP() {
        return sp;
    }

    public void setSP(byte sp) {
        this.sp = sp;
    }

    public byte getPCL() {
        return pcl;
    }

    public void setPCL(byte pcl) {
        this.pcl = pcl;
    }

    public byte getPCH() {
        return pch;
    }

    public void setPCH(byte pch) {
        this.pch = pch;
    }

    public void setShowDebug(boolean showDebug) {
        this.showDebug = showDebug;
    }

    public long getCycleDelay() {
        return cycleDelay;
    }

    public void setCycleDelay(long cycleDelay) {
        this.cycleDelay = cycleDelay;
    }

}
