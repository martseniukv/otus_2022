Before changes:

| Heap size (MB)     | 256              | 512   | 1024  | 2048  |
|--------------------|------------------|-------|-------|-------|
| <b>Time (mSec)<b/> | OutOfMemoryError | 13367 | 12337 | 12064 |


After changes:

| Heap size (MB)     | 256  | 512  | 1024 | 2048 |
|--------------------|------|------|------|------|
| <b>Time (mSec)<b/> | 3166 | 2308 | 2501 | 2753 |

Best time before optimization - 2048MB (Xms = Xmx)

Best time after optimization - 512MB (Xms = Xmx)