# Crispy-Rotary-Phone

A quick serialization system I made for encoding byte arrays & lists of byte arrays.

Can technically hold individual arrays up to 255^127 + 127 bytes in length. Also can hold a max of 255^127 + 127 individual arrays in one encoded list. As such, has a theoretical max size of (255^127 + 127)^2. However, Java puts a damper on things by requiring arrays to be less than the max value of an integer in size, and computers put a damper on things by having limited amounts of memory, so that's not really achievable. (Yet!)

I might add a class for a larger array at some point so that it can theoretically hold more data. However, for now, I don't want to deal with Java complaining at me.

Also supports nesting lists. Prepare for the most terrifying matryoshka in existence.

If you use this, please don't hate me when it breaks - it tends to do that every few minutes.

TO DO:

* ~~Fix the isEncoded() method. Currently works by trying to decode the byte[], and if the decoding causes an out of memory error, considers it to not be encoded. Needless to say, that's bad.~~ Complete!
* ~~Fix up for random-access!~~ Ignore that, I completely misunderstood what random-access means with regard to serialization schemes. My thinking was, ish, that i I can deserialize lists, I should be able to deserialize parts of them just by looking at the indicator for the number of elements, & the indicators for the length of each element up to the one I actually care about. without going through & processing the elements up to that point.
* Speed: Check against FlatBuffers & ProtoBuf
* Output Size: Check against FlatBuffers & ProtoBuf. Currently appears to be a few bytes smaller than protobuf, but I'm willing to bet that's because unlike protobuf, this doesn't need to indicate the data type of encoded elements.
* Give the option of encoding in the current form (Everything is a byte array and you can't convince me otherwise) or in a more normal form (this array here is the encoding of a ______ and contains ______)