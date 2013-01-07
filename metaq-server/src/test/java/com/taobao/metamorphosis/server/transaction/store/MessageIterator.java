package com.taobao.metamorphosis.server.transaction.store;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.exception.InvalidMessageException;
import com.taobao.metamorphosis.utils.MessageUtils;


/**
 * ��Ϣ������������������������ݣ���ʱΪ�������Ը���
 * 
 * @author boyan
 * @Date 2011-4-20
 * 
 */
// TODO �������clientǨ�Ƶ�commons
public class MessageIterator {
    private final String topic;
    private final byte[] data;
    private int offset;


    public MessageIterator(final String topic, final byte[] data) {
        super();
        this.topic = topic;
        this.data = data;
        this.offset = 0;
    }


    /**
     * just for test
     * 
     * @param offset
     */
    void setOffset(final int offset) {
        this.offset = offset;
    }


    /**
     * ���ص�ǰ������ƫ���������������������ƫ��������
     * 
     * @return
     */
    public int getOffset() {
        return this.offset;
    }


    /**
     * ��������Ϣ��ʱ�򷵻�true
     * 
     * @return
     */
    public boolean hasNext() {
        if (this.data == null || this.data.length == 0) {
            return false;
        }
        if (this.offset >= this.data.length) {
            return false;
        }
        if (this.data.length - this.offset < MessageUtils.HEADER_LEN) {
            return false;
        }
        final int msgLen = MessageUtils.getInt(this.offset, this.data);
        if (this.data.length - this.offset - MessageUtils.HEADER_LEN < msgLen) {
            return false;
        }
        return true;

    }


    /**
     * ������һ����Ϣ
     * 
     * @return
     * @throws InvalidMessageException
     */
    public Message next() throws InvalidMessageException {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        final MessageUtils.DecodedMessage decodeMessage =
                MessageUtils.decodeMessage(this.topic, this.data, this.offset);
        this.offset = decodeMessage.newOffset;
        return decodeMessage.message;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.data);
        result = prime * result + this.offset;
        result = prime * result + (this.topic == null ? 0 : this.topic.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final MessageIterator other = (MessageIterator) obj;
        if (!Arrays.equals(this.data, other.data)) {
            return false;
        }
        if (this.offset != other.offset) {
            return false;
        }
        if (this.topic == null) {
            if (other.topic != null) {
                return false;
            }
        }
        else if (!this.topic.equals(other.topic)) {
            return false;
        }
        return true;
    }


    public void remove() {
        throw new UnsupportedOperationException("Unsupported remove");

    }

}