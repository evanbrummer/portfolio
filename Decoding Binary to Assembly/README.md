# LEGv8 Binary Decoder
(LEGv8, a simplification of ARM for classwork)

This was a pair project I worked on COM S 321, intro to computer architecture. We had to turn machine code executables back into the assembly source code they were generated from. 

Fortunately I was already pretty familiar with bitwise operations, but this project really cemented my understanding. Since each instruction was 32 bits long, we used a data stream to read integers on a loop until the end of the file. After decoding all the instructions, we made another pass to add the branch labels.

### My code

- [prog2.java]() lines `1-171`, `187-212`, `233-257`, `316-344`, `363-384`.