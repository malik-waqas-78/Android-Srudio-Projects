package com.ppt.walkie.callbacks

import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import com.ppt.walkie.services.ConnectionsServiceOKRA

interface ServiceCallBacksOKRA {
    fun onEndpointDiscovered(endpoint: ConnectionsServiceOKRA.Endpoint?)
    fun onConnectionInitiated(
        endpoint: ConnectionsServiceOKRA.Endpoint?,
        connectionInfo: ConnectionInfo
    )

    fun onEndpointConnected(endpoint: ConnectionsServiceOKRA.Endpoint)
    fun onEndpointDisconnected(endpoint: ConnectionsServiceOKRA.Endpoint)
    fun onConnectionFailed(endpoint: ConnectionsServiceOKRA.Endpoint?)
    fun onReceive(endpoint: ConnectionsServiceOKRA.Endpoint?, payload: Payload)
    fun getName(): String?
    fun getServiceId(): String?
    fun getStrategy(): Strategy?
    fun onFinishedPlaying()
    fun onStopRecording()
    fun onStopPlaying()
    fun onStartRecording()
    fun onStateSearching()
    fun onStateConnected()
    fun onStateUnKnow()
}