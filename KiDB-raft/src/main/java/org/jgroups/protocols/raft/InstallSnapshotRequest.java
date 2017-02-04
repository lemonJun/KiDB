package org.jgroups.protocols.raft;

import java.io.DataInput;
import java.io.DataOutput;

import org.jgroups.Address;
import org.jgroups.util.Bits;
import org.jgroups.util.Util;

/**
 * @author Bela Ban
 * @since  0.1
 */
public class InstallSnapshotRequest extends RaftHeader {
    protected Address leader;
    protected int last_included_index;
    protected int last_included_term;

    public InstallSnapshotRequest() {
    }

    public InstallSnapshotRequest(int term) {
        super(term);
    }

    public InstallSnapshotRequest(int term, Address leader, int last_included_index, int last_included_term) {
        this(term);
        this.leader = leader;
        this.last_included_index = last_included_index;
        this.last_included_term = last_included_term;
    }

    @Override
    public int size() {
        return super.size() + Util.size(leader) + Bits.size(last_included_index) + Bits.size(last_included_term);
    }

    @Override
    public void writeTo(DataOutput out) throws Exception {
        super.writeTo(out);
        Util.writeAddress(leader, out);
        Bits.writeInt(last_included_index, out);
        Bits.writeInt(last_included_term, out);
    }

    @Override
    public void readFrom(DataInput in) throws Exception {
        super.readFrom(in);
        leader = Util.readAddress(in);
        last_included_index = Bits.readInt(in);
        last_included_term = Bits.readInt(in);
    }

    @Override
    public String toString() {
        return super.toString() + ", leader=" + leader + ", last_included_index=" + last_included_index + ", last_included_term=" + last_included_term;
    }
}
