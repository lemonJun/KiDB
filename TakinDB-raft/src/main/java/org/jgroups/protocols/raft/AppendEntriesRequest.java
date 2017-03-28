package org.jgroups.protocols.raft;

import java.io.DataInput;
import java.io.DataOutput;

import org.jgroups.Address;
import org.jgroups.Global;
import org.jgroups.util.Bits;
import org.jgroups.util.Util;

/**
 * Used to send AppendEntries messages to cluster members. The log entries are contained in actual payload of the message,
 * not in this header.
 * @author Bela Ban
 * @since  0.1
 */
public class AppendEntriesRequest extends RaftHeader {
    protected Address leader; // probably not needed as msg.src() contains the leader's address already
    protected int prev_log_index;
    protected int prev_log_term;
    protected int entry_term; // term of the given entry, e.g. when relaying a log to a late joiner
    protected int leader_commit; // the commit_index of the leader
    protected boolean internal;

    public AppendEntriesRequest() {
    }

    public AppendEntriesRequest(int term, Address leader, int prev_log_index, int prev_log_term, int entry_term, int leader_commit, boolean internal) {
        super(term);
        this.leader = leader;
        this.prev_log_index = prev_log_index;
        this.prev_log_term = prev_log_term;
        this.entry_term = entry_term;
        this.leader_commit = leader_commit;
        this.internal = internal;
    }

    @Override
    public int size() {
        return super.size() + Util.size(leader) + Bits.size(prev_log_index) + Bits.size(prev_log_term) + Bits.size(entry_term) + Bits.size(leader_commit) + Global.BYTE_SIZE;
    }

    @Override
    public void writeTo(DataOutput out) throws Exception {
        super.writeTo(out);
        Util.writeAddress(leader, out);
        Bits.writeInt(prev_log_index, out);
        Bits.writeInt(prev_log_term, out);
        Bits.writeInt(entry_term, out);
        Bits.writeInt(leader_commit, out);
        out.writeBoolean(internal);
    }

    @Override
    public void readFrom(DataInput in) throws Exception {
        super.readFrom(in);
        leader = Util.readAddress(in);
        prev_log_index = Bits.readInt(in);
        prev_log_term = Bits.readInt(in);
        entry_term = Bits.readInt(in);
        leader_commit = Bits.readInt(in);
        internal = in.readBoolean();
    }

    @Override
    public String toString() {
        return super.toString() + ", leader=" + leader + ", prev_log_index=" + prev_log_index + ", prev_log_term=" + prev_log_term + ", entry_term=" + entry_term + ", leader_commit=" + leader_commit + ", internal=" + internal;
    }
}
