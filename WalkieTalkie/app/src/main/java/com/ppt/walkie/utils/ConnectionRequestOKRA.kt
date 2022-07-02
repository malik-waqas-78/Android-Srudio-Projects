package com.ppt.walkie.utils

import com.google.android.gms.nearby.connection.ConnectionInfo
import com.ppt.walkie.services.ConnectionsServiceOKRA

class ConnectionRequestOKRA(var requestingEndpoint: ConnectionsServiceOKRA.Endpoint, var mConnectionInfo: ConnectionInfo) {
}