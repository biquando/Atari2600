package Atari2600.Memory;

import Atari2600.MOS6502.CPU;
import Atari2600.TV.CRT;

/**
 * Controls the visuals, audio, and input of the system.
 *
 * The Television Interface Adaptor (TIA) is the custom computer chip that
 * is the heart of the Atari 2600 game console, generating the screen display,
 * sound effects, and reading input controllers. (from wikipedia)
 */
public class TIA extends MemSection implements Runnable {

    private CRT crt;
    private CPU cpu;
    private Integer[] colors;
    private volatile boolean ready, WSYNC;
    private boolean[] playfield, ctrlpf, grp0, grp1;
    private int rp0, rp1;

    // Colors
    private int coluBK, coluPF, coluP0, coluP1;

    public TIA() {
        super(0x0, 128);

        colors = new Integer[256];
        setupColors();

        playfield = new boolean[80];
        ctrlpf = new boolean[5];

        grp0 = new boolean[8];
        grp1 = new boolean[8];
    }

    /**
     * Writes to the TIA registers.
     * Writing to registers has side effects, such as waiting
     * for the horizontal blank if 0x02 (WSYNC) is written to.
     *
     * This method is called on cpu-thread.
     *
     * @param data byte to be written
     * @param addr address where data should be written [0x00–0x7F]
     */
    @Override
    public void write(byte data, byte addr) {
        super.write(data, addr);
        int d = (int)data & 0xff, a = (int)addr & 0xff;

        switch (a) {
            case 0x00: // VSYNC
                crt.vsync = (d >> 1) % 2 == 1;
                break;
            case 0x01: // VBLANK
                crt.vblank = (d >> 1) % 2 == 1;
                break;
            case 0x02: // WSYNC
//                if (!(!crt.vsync && crt.getScanline() < 3)) {
                    cpu.stop();
                    WSYNC = true;
                    while (WSYNC) {
                    }
                    cpu.restart();
//                }
                break;
            case 0x06: // COLUP0
                coluP0 = colors[d];
                break;
            case 0x07: // COLUP1
                coluP1 = colors[d];
                break;
            case 0x08: // COLUPF
                coluPF = colors[d];
                break;
            case 0x09: // COLUBK
                coluBK = colors[d];
                break;
            case 0x0A: // CTRLPF
                for (int i = 0; i < 5; i++) {
                    ctrlpf[i] = (d >> i) % 2 == 1;
                }
                break;
            case 0x0D: // PF0
                for (int i = 0; i < 4; i++) {
                    boolean pfPixel = (d >> (7 - i)) % 2 == 1;
                    i *= 4;
                    playfield[15 - i] = pfPixel;
                    playfield[14 - i] = pfPixel;
                    playfield[13 - i] = pfPixel;
                    playfield[12 - i] = pfPixel;
                    i /= 4;
                }
                break;
            case 0x0E: // PF1
                for (int i = 0; i < 8; i++) {
                    boolean pfPixel = (d >> (7 - i)) % 2 == 1;
                    i *= 4;
                    playfield[16 + i] = pfPixel;
                    playfield[17 + i] = pfPixel;
                    playfield[18 + i] = pfPixel;
                    playfield[19 + i] = pfPixel;
                    i /= 4;
                }
                break;
            case 0x0F: // PF2
                for (int i = 0; i < 8; i++) {
                    boolean pfPixel = (d >> i) % 2 == 1;
                    i *= 4;
                    playfield[48 + i] = pfPixel;
                    playfield[49 + i] = pfPixel;
                    playfield[50 + i] = pfPixel;
                    playfield[51 + i] = pfPixel;
                    i /= 4;
                }
                break;
            case 0x10: // RESP0
                rp0 = crt.getCycle();
                break;
            case 0x11: // RESP1
                rp1 = crt.getCycle();
                break;
            case 0x1B: // GRP0
                for (int i = 7; i >= 0; i--) {
                    grp0[7 - i] = (d >> i) % 2 == 1;
                }
                break;
            case 0x1C: // GRP1
                for (int i = 7; i >= 0; i--) {
                    grp1[7 - i] = (d >> i) % 2 == 1;
                }
                break;
        }
    }

    /**
     * Advances the connected CRT by 3 TIA clock cycles.
     */
    public void step() {
        for (int i = 0; i < 3; i++) {
            int cycle = crt.getCycle();

            // Background
            crt.setColor(coluBK);

            // Playfield
            drawPF(cycle);

            // Players
            drawP0(cycle);
            drawP1(cycle);


            crt.step();
        }
        ready = false;
    }

    private void drawPF(int cycle) {
        boolean drawPF = false;
        if (cycle >= 148) {
            if (ctrlpf[0]) {
                drawPF = playfield[227 - cycle];
            } else {
                drawPF = playfield[cycle - 148];
            }
        } else if (cycle >= 68) {
            drawPF = playfield[cycle - 68];
        }
        if (drawPF) {
            crt.setColor(coluPF);
        }
    }

    private void drawP0(int cycle) {
        if (cycle >= rp0 && cycle < rp0 + 8 && grp0[cycle - rp0]) {
            crt.setColor(coluP0);
        }
    }

    private void drawP1(int cycle) {
        if (cycle >= rp1 && cycle < rp1 + 8 && grp1[cycle - rp1]) {
            crt.setColor(coluP1);
        }
    }

    //--------------------------------------------------------------------------

    @Override
    public void run() {
        while (true) {
            if (WSYNC) {
                int sc = crt.getScanline();
                while (sc == crt.getScanline()) {
                    step();
                }
                WSYNC = false;
            }
            if (ready) {
                step();
            }
        }
    }

    /**
     * Connects the TIA to a CRT.
     */
    public void use(CRT crt) {
        this.crt = crt;
    }

    /**
     * Connects the TIA to a CPU.
     */
    public void use(CPU cpu) { this.cpu = cpu; }

    /**
     * Used by the CPU every cycle to signal the TIA to do its 3 cycles.
     */
    public void sync() {
        ready = true;
    }

    private void setupColors() {
        colors[0x0] = 0x000000;
        colors[0x1] = 0x000000;
        colors[0x2] = 0x404040;
        colors[0x3] = 0x404040;
        colors[0x4] = 0x6c6c6c;
        colors[0x5] = 0x6c6c6c;
        colors[0x6] = 0x909090;
        colors[0x7] = 0x909090;
        colors[0x8] = 0xb0b0b0;
        colors[0x9] = 0xb0b0b0;
        colors[0xa] = 0xc8c8c8;
        colors[0xb] = 0xc8c8c8;
        colors[0xc] = 0xdcdcdc;
        colors[0xd] = 0xdcdcdc;
        colors[0xe] = 0xececec;
        colors[0xf] = 0xececec;
        colors[0x10] = 0x444400;
        colors[0x11] = 0x444400;
        colors[0x12] = 0x646410;
        colors[0x13] = 0x646410;
        colors[0x14] = 0x848424;
        colors[0x15] = 0x848424;
        colors[0x16] = 0xa0a034;
        colors[0x17] = 0xa0a034;
        colors[0x18] = 0xb8b840;
        colors[0x19] = 0xb8b840;
        colors[0x1a] = 0xd0d050;
        colors[0x1b] = 0xd0d050;
        colors[0x1c] = 0xe8e85c;
        colors[0x1d] = 0xe8e85c;
        colors[0x1e] = 0xfcfc68;
        colors[0x1f] = 0xfcfc68;
        colors[0x20] = 0x702800;
        colors[0x21] = 0x702800;
        colors[0x22] = 0x844414;
        colors[0x23] = 0x844414;
        colors[0x24] = 0x985c28;
        colors[0x25] = 0x985c28;
        colors[0x26] = 0xac783c;
        colors[0x27] = 0xac783c;
        colors[0x28] = 0xbc8c4c;
        colors[0x29] = 0xbc8c4c;
        colors[0x2a] = 0xcca05c;
        colors[0x2b] = 0xcca05c;
        colors[0x2c] = 0xdcb468;
        colors[0x2d] = 0xdcb468;
        colors[0x2e] = 0xecc878;
        colors[0x2f] = 0xecc878;
        colors[0x30] = 0x841800;
        colors[0x31] = 0x841800;
        colors[0x32] = 0x983418;
        colors[0x33] = 0x983418;
        colors[0x34] = 0xac5030;
        colors[0x35] = 0xac5030;
        colors[0x36] = 0xc06848;
        colors[0x37] = 0xc06848;
        colors[0x38] = 0xd0805c;
        colors[0x39] = 0xd0805c;
        colors[0x3a] = 0xe09470;
        colors[0x3b] = 0xe09470;
        colors[0x3c] = 0xeca880;
        colors[0x3d] = 0xeca880;
        colors[0x3e] = 0xfcbc94;
        colors[0x3f] = 0xfcbc94;
        colors[0x40] = 0x880000;
        colors[0x41] = 0x880000;
        colors[0x42] = 0x9c2020;
        colors[0x43] = 0x9c2020;
        colors[0x44] = 0xb03c3c;
        colors[0x45] = 0xb03c3c;
        colors[0x46] = 0xc05858;
        colors[0x47] = 0xc05858;
        colors[0x48] = 0xd07070;
        colors[0x49] = 0xd07070;
        colors[0x4a] = 0xe08888;
        colors[0x4b] = 0xe08888;
        colors[0x4c] = 0xeca0a0;
        colors[0x4d] = 0xeca0a0;
        colors[0x4e] = 0xfcb4b4;
        colors[0x4f] = 0xfcb4b4;
        colors[0x50] = 0x78005c;
        colors[0x51] = 0x78005c;
        colors[0x52] = 0x8c2074;
        colors[0x53] = 0x8c2074;
        colors[0x54] = 0xa03c88;
        colors[0x55] = 0xa03c88;
        colors[0x56] = 0xb0589c;
        colors[0x57] = 0xb0589c;
        colors[0x58] = 0xc070b0;
        colors[0x59] = 0xc070b0;
        colors[0x5a] = 0xd084c0;
        colors[0x5b] = 0xd084c0;
        colors[0x5c] = 0xdc9cd0;
        colors[0x5d] = 0xdc9cd0;
        colors[0x5e] = 0xecb0e0;
        colors[0x5f] = 0xecb0e0;
        colors[0x60] = 0x480078;
        colors[0x61] = 0x480078;
        colors[0x62] = 0x602090;
        colors[0x63] = 0x602090;
        colors[0x64] = 0x783ca4;
        colors[0x65] = 0x783ca4;
        colors[0x66] = 0x8c58b8;
        colors[0x67] = 0x8c58b8;
        colors[0x68] = 0xa070cc;
        colors[0x69] = 0xa070cc;
        colors[0x6a] = 0xb484dc;
        colors[0x6b] = 0xb484dc;
        colors[0x6c] = 0xc49cec;
        colors[0x6d] = 0xc49cec;
        colors[0x6e] = 0xd4b0fc;
        colors[0x6f] = 0xd4b0fc;
        colors[0x70] = 0x140084;
        colors[0x71] = 0x140084;
        colors[0x72] = 0x302098;
        colors[0x73] = 0x302098;
        colors[0x74] = 0x4c3cac;
        colors[0x75] = 0x4c3cac;
        colors[0x76] = 0x6858c0;
        colors[0x77] = 0x6858c0;
        colors[0x78] = 0x7c70d0;
        colors[0x79] = 0x7c70d0;
        colors[0x7a] = 0x9488e0;
        colors[0x7b] = 0x9488e0;
        colors[0x7c] = 0xa8a0ec;
        colors[0x7d] = 0xa8a0ec;
        colors[0x7e] = 0xbcb4fc;
        colors[0x7f] = 0xbcb4fc;
        colors[0x80] = 0x000088;
        colors[0x81] = 0x000088;
        colors[0x82] = 0x1c209c;
        colors[0x83] = 0x1c209c;
        colors[0x84] = 0x3840b0;
        colors[0x85] = 0x3840b0;
        colors[0x86] = 0x505cc0;
        colors[0x87] = 0x505cc0;
        colors[0x88] = 0x6874d0;
        colors[0x89] = 0x6874d0;
        colors[0x8a] = 0x7c8ce0;
        colors[0x8b] = 0x7c8ce0;
        colors[0x8c] = 0x90a4ec;
        colors[0x8d] = 0x90a4ec;
        colors[0x8e] = 0xa4b8fc;
        colors[0x8f] = 0xa4b8fc;
        colors[0x90] = 0x00187c;
        colors[0x91] = 0x00187c;
        colors[0x92] = 0x1c3890;
        colors[0x93] = 0x1c3890;
        colors[0x94] = 0x3854a8;
        colors[0x95] = 0x3854a8;
        colors[0x96] = 0x5070bc;
        colors[0x97] = 0x5070bc;
        colors[0x98] = 0x6888cc;
        colors[0x99] = 0x6888cc;
        colors[0x9a] = 0x7c9cdc;
        colors[0x9b] = 0x7c9cdc;
        colors[0x9c] = 0x90b4ec;
        colors[0x9d] = 0x90b4ec;
        colors[0x9e] = 0xa4c8fc;
        colors[0x9f] = 0xa4c8fc;
        colors[0xa0] = 0x002c5c;
        colors[0xa1] = 0x002c5c;
        colors[0xa2] = 0x1c4c78;
        colors[0xa3] = 0x1c4c78;
        colors[0xa4] = 0x386890;
        colors[0xa5] = 0x386890;
        colors[0xa6] = 0x5084ac;
        colors[0xa7] = 0x5084ac;
        colors[0xa8] = 0x689cc0;
        colors[0xa9] = 0x689cc0;
        colors[0xaa] = 0x7cb4d4;
        colors[0xab] = 0x7cb4d4;
        colors[0xac] = 0x90cce8;
        colors[0xad] = 0x90cce8;
        colors[0xae] = 0xa4e0fc;
        colors[0xaf] = 0xa4e0fc;
        colors[0xb0] = 0x003c2c;
        colors[0xb1] = 0x003c2c;
        colors[0xb2] = 0x1c5c48;
        colors[0xb3] = 0x1c5c48;
        colors[0xb4] = 0x387c64;
        colors[0xb5] = 0x387c64;
        colors[0xb6] = 0x509c80;
        colors[0xb7] = 0x509c80;
        colors[0xb8] = 0x68b494;
        colors[0xb9] = 0x68b494;
        colors[0xba] = 0x7cd0ac;
        colors[0xbb] = 0x7cd0ac;
        colors[0xbc] = 0x90e4c0;
        colors[0xbd] = 0x90e4c0;
        colors[0xbe] = 0xa4fcd4;
        colors[0xbf] = 0xa4fcd4;
        colors[0xc0] = 0x003c00;
        colors[0xc1] = 0x003c00;
        colors[0xc2] = 0x205c20;
        colors[0xc3] = 0x205c20;
        colors[0xc4] = 0x407c40;
        colors[0xc5] = 0x407c40;
        colors[0xc6] = 0x5c9c5c;
        colors[0xc7] = 0x5c9c5c;
        colors[0xc8] = 0x74b474;
        colors[0xc9] = 0x74b474;
        colors[0xca] = 0x8cd08c;
        colors[0xcb] = 0x8cd08c;
        colors[0xcc] = 0xa4e4a4;
        colors[0xcd] = 0xa4e4a4;
        colors[0xce] = 0xb8fcb8;
        colors[0xcf] = 0xb8fcb8;
        colors[0xd0] = 0x143800;
        colors[0xd1] = 0x143800;
        colors[0xd2] = 0x345c1c;
        colors[0xd3] = 0x345c1c;
        colors[0xd4] = 0x507c38;
        colors[0xd5] = 0x507c38;
        colors[0xd6] = 0x6c9850;
        colors[0xd7] = 0x6c9850;
        colors[0xd8] = 0x84b468;
        colors[0xd9] = 0x84b468;
        colors[0xda] = 0x9ccc7c;
        colors[0xdb] = 0x9ccc7c;
        colors[0xdc] = 0xb4e490;
        colors[0xdd] = 0xb4e490;
        colors[0xde] = 0xc8fca4;
        colors[0xdf] = 0xc8fca4;
        colors[0xe0] = 0x2c3000;
        colors[0xe1] = 0x2c3000;
        colors[0xe2] = 0x4c501c;
        colors[0xe3] = 0x4c501c;
        colors[0xe4] = 0x687034;
        colors[0xe5] = 0x687034;
        colors[0xe6] = 0x848c4c;
        colors[0xe7] = 0x848c4c;
        colors[0xe8] = 0x9ca864;
        colors[0xe9] = 0x9ca864;
        colors[0xea] = 0xb4c078;
        colors[0xeb] = 0xb4c078;
        colors[0xec] = 0xccd488;
        colors[0xed] = 0xccd488;
        colors[0xee] = 0xe0ec9c;
        colors[0xef] = 0xe0ec9c;
        colors[0xf0] = 0x442800;
        colors[0xf1] = 0x442800;
        colors[0xf2] = 0x644818;
        colors[0xf3] = 0x644818;
        colors[0xf4] = 0x846830;
        colors[0xf5] = 0x846830;
        colors[0xf6] = 0xa08444;
        colors[0xf7] = 0xa08444;
        colors[0xf8] = 0xb89c58;
        colors[0xf9] = 0xb89c58;
        colors[0xfa] = 0xd0b46c;
        colors[0xfb] = 0xd0b46c;
        colors[0xfc] = 0xe8cc7c;
        colors[0xfd] = 0xe8cc7c;
        colors[0xfe] = 0xfce08c;
        colors[0xff] = 0xfce08c;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
