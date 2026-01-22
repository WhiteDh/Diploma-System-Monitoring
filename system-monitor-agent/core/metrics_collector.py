import psutil
#import wmi
def collect_metrics(agent_id):
    metrics = {
        "agentId": agent_id,
        "cpuUsage": psutil.cpu_percent(),
        "ramUsage": psutil.virtual_memory().percent,

        # !!for this, you need programme named wmi, i`ve already deleted it!!
        #so the temparature will be 0 =D
        #"temperature":  get_cpu_temperature(),
        "temperature": 0,
        "diskUsage": psutil.disk_usage('/').percent,
        "networkIn": psutil.net_io_counters().bytes_recv / 1024 / 1024,
        "networkOut": psutil.net_io_counters().bytes_sent / 1024 / 1024
    }
    return metrics

# def get_cpu_temperature():
#     w = wmi.WMI(namespace="root\\OpenHardwareMonitor")
#     sensors = w.Sensor()
#     for sensor in sensors:
#         if sensor.SensorType == u'Temperature' and "CPU" in sensor.Name:
#             return sensor.Value
#     return None

