# Chaos
本算法对源文使用ascii表进行轮替，加密效果由密钥的组成和长度决定。对于一段密文，给出不同的密钥会得到不同的解，只有正确的密钥才能得到正确的解。更适合处理具有大量文本信息的数据。对于明文较短的情况，先对源文做AES加密，再用本算法，可以有效保护AES的加密结果，令其不可破解。
密钥不能是同一个字符组成，长度不能低于4，6以上效果更好。
这段Java代码以字节为单位进行处理，如果可以更小，效果会更好。

translate by baidu:
This algorithm uses ASCII table to rotate the source text, and the encryption effect is determined by the composition and length of the key. For a ciphertext, giving different keys will get different solutions. Only the correct key can get the correct solution. It is more suitable for processing data with a large amount of text information.
For the case of short plaintext, the source text is encrypted by AES first, and then this algorithm can effectively protect the encryption result of AES.
The key cannot be composed of the same character, and the length cannot be less than 4 or 6. The effect is better.
Java processed byte as unit, the effect will be better if it can be smaller.
