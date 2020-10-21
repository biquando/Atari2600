package Atari2600;

import Atari2600.MOS6502.CPU;
import Atari2600.Memory.Memory;
import Atari2600.TV.CRT;

import java.io.File;

public class Tester {
    public static void main(String[] args) {
        Memory mem = new Memory();
        mem.connectCRT(new CRT(6, 3));
        CPU cpu = new CPU(mem);
//        cpu.setShowDebug(true);
//        cpu.setCycleDelay(100000);

//        File f = new File("D:\\Documents\\Programming\\Atari2600\\kernel_15.bin");
        File f = new File("D:\\Documents\\Programming\\Atari2600\\dasm-2.20.13-win-x64\\test.bin");

//        File f = new File("D:\\Documents\\Programming\\Atari2600\\Donkey Kong.bin");
//        File f = new File("D:\\Documents\\Programming\\Atari2600\\Pac-Man.bin");
//        File f = new File("D:\\Documents\\Programming\\Atari2600\\Dungeon2.bin");
//        File f = new File("D:\\Documents\\Programming\\Atari2600\\Dragster.bin");
        mem.loadCartridge(f);

        Thread t = new Thread(cpu, "cpu-thread");
        t.start();
    }
}
