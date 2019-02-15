package io.netty.mitix.rpc.v3.common;

import java.util.Arrays;

/**
 *
 */
public class RpcRequest {
    //调用ID
    private String id;

    private String serviceName;

    private String methodName;
    //类型
    private transient Class<?>[] parameterTypes;
    //类型名字
    private String[] parameterTypeNames;
    //参数
    private Object[] args;

    public RpcRequest() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String[] getParameterTypeNames() {
        return parameterTypeNames;
    }

    public void setParameterTypeNames(String[] parameterTypeNames) {
        this.parameterTypeNames = parameterTypeNames;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = prime * hash + ((id == null) ? 0 : id.hashCode());
        hash = prime * hash + ((methodName == null) ? 0 : methodName.hashCode());
        hash = prime * hash + ((serviceName == null) ? 0 : serviceName.hashCode());
        hash = prime * hash + Arrays.hashCode(args);
        hash = prime * hash + Arrays.hashCode(parameterTypeNames);
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
        RpcRequest other = (RpcRequest) obj;
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
        if (!Arrays.equals(this.args, ((RpcRequest) obj).getArgs())) {
            return false;
        }
        if (!Arrays.equals(this.parameterTypeNames, ((RpcRequest) obj).getParameterTypeNames())) {
            return false;
        }
        if (serviceName == null) {
            if (other.serviceName != null) {
                return false;
            }
        } else if (!serviceName.equals(other.serviceName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "id='" + id + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameterTypeNames=" + Arrays.toString(parameterTypeNames) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}