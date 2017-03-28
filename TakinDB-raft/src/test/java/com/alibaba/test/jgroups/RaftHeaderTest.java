package com.alibaba.test.jgroups;

import org.jgroups.Global;
import org.jgroups.protocols.raft.AppendEntriesRequest;
import org.jgroups.protocols.raft.AppendEntriesResponse;
import org.jgroups.protocols.raft.AppendResult;
import org.jgroups.protocols.raft.HeartbeatRequest;
import org.jgroups.protocols.raft.InstallSnapshotRequest;
import org.jgroups.protocols.raft.REDIRECT;
import org.jgroups.protocols.raft.RaftHeader;
import org.jgroups.protocols.raft.VoteRequest;
import org.jgroups.protocols.raft.VoteResponse;
import org.jgroups.util.ByteArrayDataOutputStream;
import org.jgroups.util.Util;
import org.testng.annotations.Test;

/**
 * @author Bela Ban
 * @since  0.1
 */
@Test(groups = Global.FUNCTIONAL, singleThreaded = false)
public class RaftHeaderTest {

    public void testVoteRequestHeader() throws Exception {
        VoteRequest hdr = new VoteRequest(22, 3, 7);
        _testSize(hdr, VoteRequest.class);
    }

    public void testVoteResponseHeader() throws Exception {
        VoteResponse rsp = new VoteResponse(22, true);
        _testSize(rsp, VoteResponse.class);
    }

    public void testHeatbeatHeader() throws Exception {
        HeartbeatRequest hb = new HeartbeatRequest(22, Util.createRandomAddress("A"));
        _testSize(hb, HeartbeatRequest.class);
    }

    public void testAppendEntriesRequest() throws Exception {
        AppendEntriesRequest req = new AppendEntriesRequest(22, Util.createRandomAddress("A"), 4, 21, 22, 18, true);
        _testSize(req, AppendEntriesRequest.class);
    }

    public void testAppendEntriesResponse() throws Exception {
        AppendEntriesResponse rsp = new AppendEntriesResponse(22, new AppendResult(false, 22, 5));
        _testSize(rsp, AppendEntriesResponse.class);
    }

    public void testInstallSnapshotHeader() throws Exception {
        InstallSnapshotRequest hdr = new InstallSnapshotRequest(5);
        _testSize(hdr, InstallSnapshotRequest.class);

        hdr = new InstallSnapshotRequest(5, Util.createRandomAddress("A"), 5, 4);
        _testSize(hdr, InstallSnapshotRequest.class);
    }

    public static void testRedirectHeader() throws Exception {
        REDIRECT.RedirectHeader hdr = new REDIRECT.RedirectHeader(REDIRECT.RequestType.SET_REQ, 22, true);
        _testSize(hdr, REDIRECT.RedirectHeader.class);

        hdr = new REDIRECT.RedirectHeader(REDIRECT.RequestType.RSP, 322649, false);
        _testSize(hdr, REDIRECT.RedirectHeader.class);
    }

    protected static <T extends RaftHeader> void _testSize(T hdr, Class<T> clazz) throws Exception {
        int size = hdr.size();
        ByteArrayDataOutputStream out = new ByteArrayDataOutputStream(size);
        hdr.writeTo(out);
        System.out.println(clazz.getSimpleName() + ": size=" + size);
        assert out.position() == size;

        RaftHeader hdr2 = (RaftHeader) Util.streamableFromByteBuffer(clazz, out.buffer(), 0, out.position());
        assert hdr2 != null;
        assert hdr.term() == hdr2.term();
    }

    protected static <T extends REDIRECT.RedirectHeader> void _testSize(T hdr, Class<T> clazz) throws Exception {
        int size = hdr.size();
        ByteArrayDataOutputStream out = new ByteArrayDataOutputStream(size);
        hdr.writeTo(out);
        System.out.println(clazz.getSimpleName() + ": size=" + size);
        assert out.position() == size;

        REDIRECT.RedirectHeader hdr2 = (REDIRECT.RedirectHeader) Util.streamableFromByteBuffer(clazz, out.buffer(), 0, out.position());
        assert hdr2 != null;
        assert hdr.size() == hdr2.size();
    }
}
