package general.algorithms;

public enum BlockSizeAES {
    BLOCK_SIZE_AES(16), BLOCK_SIZE_ECB(128);

    private final int size;

    BlockSizeAES(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
