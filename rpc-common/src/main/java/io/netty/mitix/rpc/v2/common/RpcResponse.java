package io.netty.mitix.rpc.v2.common;

/**
 *
 */
public class RpcResponse {
    private String id;

    private String serviceName;

    private String methodName;

    private String cause;

    private Object result;

    public RpcResponse() {
        super();
    }


    public String getId() {
        return id;
    }


    public String getServiceName() {
        return serviceName;
    }


    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = prime * hash + ((cause == null) ? 0 : cause.hashCode());
        hash = prime * hash + ((id == null) ? 0 : id.hashCode());
        hash = prime * hash + ((methodName == null) ? 0 : methodName.hashCode());
        hash = prime * hash + (getResult() == null ? 0 : getResult().hashCode());
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RpcResponse other = (RpcResponse) obj;
        if (cause == null) {
            if (other.cause != null) {
                return false;
            }
        } else if (!cause.equals(other.cause)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (methodName == null) {
            if (other.methodName != null) {
                return false;
            }
        } else if (!methodName.equals(other.methodName)) {
            return false;
        }
        if (!this.equals(other.getResult())) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "RpcResponse [id=" + id + ", methodName=" + methodName + ", cause=" + cause + ", result=" + result + "]";
    }


}
