# NX so decompiler

**NX so decompiler** is an on-device reverse-engineering toolkit for Android. It opens and analyzes
native shared libraries (`.so`), APK/DEX files, ELF executables, object files, Windows PE binaries
(EXE/DLL/SYS) and more — directly on your phone.

> This project is a modernized fork of the open-source
> [Android-Disassembler](https://github.com/KYHSGeekCode/Android-Disassembler) by Hyeonseo Yang
> (MIT License). It is maintained under the **NX** project to refresh the UI, remove trackers/ads,
> and fix outstanding bugs. All original copyrights are retained — see [LICENSE](LICENSE).

## Features
- Inspect ELF/PE headers, sections and symbol tables (functions, objects, …).
- Disassemble code sections (powered by [Capstone](https://github.com/aquynh/capstone)).
- Multi-arch: ARM, ARM64, x86, x86_64, MIPS, PowerPC and more.
- Jump to address by symbol name or hex address, with a back stack and "Follow Jump".
- Syntax highlighting and instruction colorizing.
- Export disassembly (raw reloadable, compilable text, analytic text, JSON, database).
- Project support and launching directly from file browsers.
- Parses IAT/EAT of PE headers.

## Building
The app is built automatically by GitHub Actions on every push — grab the debug APK from the
**Actions → CI** run artifacts (`Android-Disassembler-debug-apk`).

To build locally you need the Android SDK + NDK and initialized git submodules:

```bash
git clone --recursive https://github.com/NX-developer/NX-so-decompiler.git
cd NX-so-decompiler
./gradlew assembleDebug
```

## Credits
- Original app: [KYHSGeekCode/Android-Disassembler](https://github.com/KYHSGeekCode/Android-Disassembler)
- Disassembly engine: [Capstone](https://github.com/aquynh/capstone)
- ELF parser: [ELFIO](https://github.com/serge1/ELFIO) / [java-binutils](https://github.com/jawi/java-binutils)
- PE parser: [pecoff4j](https://github.com/kichik/pecoff4j) · smali/baksmali: [JesusFreke/smali](https://github.com/JesusFreke/smali)

## License
MIT — see [LICENSE](LICENSE). Original work © 2018 Hyeonseo Yang.
