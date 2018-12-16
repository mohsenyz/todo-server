package com.mphj.todo.models.response;

public class SavedObjectResponse {


    private SavedObjectResponse(int localId, int serverId) {
        this.localId = localId;
        this.serverId = serverId;
    }

    public static SavedObjectResponse from(int localId, int serverId) {
        return new SavedObjectResponse(localId, serverId);
    }

    public int localId;
    public int serverId;

}
