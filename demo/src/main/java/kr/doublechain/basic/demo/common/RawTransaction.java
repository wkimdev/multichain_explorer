package kr.doublechain.basic.demo.common;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 전성국 on 2018-05-28.
 */
public final class RawTransaction {
    private int version;
    private long inputCount = 0;
    private long outputCount = 0;
    private List<RawInput> inputs = new LinkedList<>();
    private List<RawOutput> outputs = new LinkedList<>();
    private long lockTime;

    @Override
    public String toString() {
        return "RawTransaction [version=" + version + ", inputCount=" + inputCount + ", inputs="
                + inputs + ", outputCount=" + outputCount + ", outputs=" + outputs + ", lockTime="
                + lockTime + "]";
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setInputCount(long inputCount) {
        this.inputCount = inputCount;
    }

    public long getInputCount() {
        return inputCount;
    }

    public List<RawInput> getInputs() {
        return inputs;
    }

    public void setOutputCount(long outputCount) {
        this.outputCount = outputCount;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }

    public List<RawOutput> getOutputs() {
        return outputs;
    }

    public long getOutputCount() {
        return outputCount;
    }

    public static class VariableInt {
        int size = 0;
        long value = 0;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }

    public static VariableInt readVariableInt(byte[] data, int start) {
        if (data == null || data.length <= start) {
            return new VariableInt();
        }
        int checkSize = 0xFF & data[start];
        VariableInt varInt = new VariableInt();
        varInt.setSize(0);

        if (checkSize < 0xFD) {
            varInt.setSize(1);
            varInt.setValue(checkSize);
            return varInt;
        }

        if (checkSize == 0xFD) {
            varInt.setSize(3);
        } else if (checkSize == 0xFE) {
            varInt.setSize(5);
        } else if (checkSize == 0xFF) {
            varInt.setSize(9);
        }

        if (varInt.getSize() == 0) {
            return null;
        }

        byte[] newData = ByteUtilities.readBytes(data, start + 1, varInt.getSize() - 1);
        newData = ByteUtilities.flipEndian(newData);
        varInt.setValue(new BigInteger(1, newData).longValue());
        return varInt;
    }

    public static RawTransaction parse(String txData) {
        RawTransaction tx = new RawTransaction();
        byte[] rawTx = ByteUtilities.toByteArray(txData);
        int buffPointer = 0;

        // Version
        byte[] version = ByteUtilities.readBytes(rawTx, buffPointer, 4);
        buffPointer += 4;
        version = ByteUtilities.flipEndian(version);
        tx.setVersion(new BigInteger(1, version).intValue());

        // Number of inputs
        VariableInt varInputCount = readVariableInt(rawTx, buffPointer);
        buffPointer += varInputCount != null ? varInputCount.getSize() : 0;
        tx.setInputCount(varInputCount != null ? varInputCount.getValue() : 0);

        // Parse inputs
        for (long i = 0; i < tx.getInputCount(); i++) {
            byte[] inputData = Arrays.copyOfRange(rawTx, buffPointer, rawTx.length);
            RawInput input = RawInput.parse(ByteUtilities.toHexString(inputData));
            buffPointer += input.getDataSize();
            tx.getInputs().add(input);
        }

        // Get the number of outputs
        VariableInt varOutputCount = readVariableInt(rawTx, buffPointer);
        buffPointer += varOutputCount != null ? varOutputCount.getSize() : 0;
        tx.setOutputCount(varOutputCount != null ? varOutputCount.getValue() : 0);

        // Parse outputs
        for (long i = 0; i < tx.getOutputCount(); i++) {
            byte[] outputData = Arrays.copyOfRange(rawTx, buffPointer, rawTx.length);
            RawOutput output = RawOutput.parse(ByteUtilities.toHexString(outputData));
            buffPointer += output.getDataSize();
            tx.getOutputs().add(output);
        }

        // Parse lock time
        byte[] lockBytes = ByteUtilities.readBytes(rawTx, buffPointer, 4);
        //buffPointer += 4;
        lockBytes = ByteUtilities.flipEndian(lockBytes);
        tx.setLockTime(new BigInteger(1, lockBytes).longValue());

        return tx;
    }
    public static byte[] writeVariableInt(long data) {
        byte[] newData;

        if (data < 0x00FD) {
            newData = new byte[1];
            newData[0] = (byte) (data & 0xFF);
        } else if (data <= 0xFFFF) {
            newData = new byte[3];
            newData[0] = (byte) 0xFD;
        } else if (data <= 4294967295L /* 0xFFFFFFFF */) {
            newData = new byte[5];
            newData[0] = (byte) 0xFE;
        } else {
            newData = new byte[9];
            newData[0] = (byte) 0xFF;
        }

        byte[] intData = BigInteger.valueOf(data).toByteArray();
        intData = ByteUtilities.stripLeadingNullBytes(intData);
        intData = ByteUtilities.leftPad(intData, newData.length - 1, (byte) 0x00);
        intData = ByteUtilities.flipEndian(intData);

        System.arraycopy(intData, 0, newData, 1, newData.length - 1);

        return newData;
    }

    public static byte[] writeVariableStackInt(long data) {
        byte[] newData;

        if (data < 0x4C) {
            newData = new byte[1];
            newData[0] = (byte) (data & 0xFF);
        } else if (data <= 0xFF) {
            newData = new byte[2];
            newData[0] = (byte) 0x4C;
        } else if (data <= 0xFFFF) {
            newData = new byte[3];
            newData[0] = (byte) 0x4D;
        } else {
            newData = new byte[5];
            newData[0] = (byte) 0x4E;
        }

        byte[] intData = BigInteger.valueOf(data).toByteArray();
        intData = ByteUtilities.stripLeadingNullBytes(intData);
        intData = ByteUtilities.leftPad(intData, newData.length - 1, (byte) 0x00);
        intData = ByteUtilities.flipEndian(intData);

        System.arraycopy(intData, 0, newData, 1, newData.length - 1);

        return newData;
    }

    public static VariableInt readVariableStackInt(byte[] data, int start) {
        int checkSize = 0xFF & data[start];
        VariableInt varInt = new VariableInt();
        varInt.setSize(0);

        if (checkSize < 0x4C) {
            varInt.setSize(1);
            varInt.setValue(checkSize);
            return varInt;
        }

        if (checkSize == 0x4C) {
            varInt.setSize(2);
        } else if (checkSize == 0x4D) {
            varInt.setSize(3);
        } else if (checkSize == 0x4E) {
            varInt.setSize(5);
        } else {
            // Just process the byte and advance
            varInt.setSize(1);
            varInt.setValue(0);
            return varInt;
        }

        if (varInt.getSize() == 0) {
            return null;
        }

        byte[] newData = ByteUtilities.readBytes(data, start + 1, varInt.getSize() - 1);
        newData = ByteUtilities.flipEndian(newData);
        varInt.setValue(new BigInteger(1, newData).longValue());
        return varInt;
    }
}
