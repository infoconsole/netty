package io.netty.mitix.rpc.v2.common;

public class RpcException extends RuntimeException {

	public RpcException(String msg) {
		super(msg);
	}

	public RpcException(String message, Exception e) {
		super(message, e);
 	}

}
