# clj.qiniu

A Clojure library for qiniu.com storage that wraps [qiniu java-sdk](https://github.com/qiniu/java-sdk).

![alt test coverage](https://raw.github.com/killme2008/clj.qiniu/master/coverage.png)

## Usage

Leiningen dependency:

```clojure
	[clj.qiniu "0.1.0-RC2"]
```

require it in your namespace:

```clojure
(require '[clj.qiniu :as qiniu])
```

### 配置

```clojure
(qiniu/set-config! :access-key "your qiniu access key" :secret-key "your qiniu secret key")
```

其他选项：

* `user-agent`:  请求的HTTP user agent值，默认`Clojure/qiniu sdk`。
* `throw-exception?`: 错误的时候是否抛出异常，默认 false，替代地返回 `{:ok false}`加上错误信息。

### 生成uptoken

```clojure
(qiniu/uptoken bucket)
(qiniu/uptoken bucket
	:expires 3600
	:scope scope
	:callbackUrl "http://exmaple.com/callback"
	:insertOnly 1
	:detectMime 1)
```

更多选项直接看源码吧。

### 上传文件

`file`参数可以是任何可以通过`clojure.java.io/input-stream`转成输入流的对象，比如 File 对象、URL、Java Resource等：

```clojure
(qiniu/upload-bucket bucket key file)
```

结果：

```clojure
{:ok true :status 200 :hash "xxxxx" :key "yyyyy" :response …… }
```

### 生成下载链接

```clojure
(qiniu/public-download-url domain key)
(qiniu/private-download-url domain key)
(qiniu/private-download-url domain key :expires (* 24 3600))
```

### 查看文件属性

```clojure
(qiniu/stat bucket key)
```

返回：

```clojure
{:hash "xxxx" :putTime 14048865740735254 :mimeType "image/jpeg"
 :status 200
 :fsize 16098}
```

### 拷贝、移动文件

```clojure
(qiniu/copy src-bucket src-key dest-bucket dest-key)
(qiniu/move src-bucket src-key dest-bucket dest-key)
```

返回：

```clojure
{:ok true ……}
```

### 删除文件：

```clojure
(qiniu/delete bucket key)
```
返回：

```clojure
{:ok true ……}
```

### 批量操作

使用`with-batch`配合`exec`搞定：

```clojure
(use '[clj.qiniu :only [with-batch stat]])
(with-batch
  (stat bucket key1)
  (stat bucket key2)
  (stat bucket key3)
  (exec))
```

返回：

```clojure
{:ok true :status 200
 :results ({:fsize 123 :hash "xxxx" ……}
             {:fsize 234 :hash "yyy" ……}
			 {:fsize 345 :hash "zzz" ……}
			 )}
```

批量拷贝、移动和删除文件也是类似：

```clojure
(with-batch
  (delete bucket key1)
  (delete bucket key2)
  (delete bucket key3)
  (exec))
```

返回：

```clojure
{:ok true :status 200
 :results ({:ok true} {:ok true} {:ok true})}
```

但是请注意，`with-batch`里的操作必须是同一种类型，不能混合。

### 图片处理

获取图片信息和 EXIF 信息：

```clojure
(image-info url)
(image-exif url)
```

获取图片缩略图：

```clojure
(image-view url :width 100 :height 100 :mode 1 :format "png")
```

返回结果的`:response`值就是缩略图的二进制数据，可以存储为文件或者输出到网页。

### 批量获取 Bucket 下的文件

根据前缀`prefix`获取 Bucket 内匹配的文件列表，`bucket-file-seq`会返回一个`LazySeq`：

```clojure
(bucket-file-seq  bucket "<prefix>" :limit 32)
```

limit设定批量查询大小，默认 32。

### Bucket 统计
查询单月统计：

```clojure
(bucket-monthly-stats bucket "201407")
```

查询某个时间范围内的空间、流量或者 API 调用统计：

```clojure
(bucket-stats bucket "space" "20140701" "20140710")
(bucket-stats bucket "transfer" "20140701" "20140710")
(bucket-stats bucket "apicall" "20140701" "20140710")
```

## License

Copyright © 2014 killme2008

Distributed under the Eclipse Public License version 1.0
