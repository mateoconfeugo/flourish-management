(ns data-formating
  (:use [clojure.test :only [is]]))

(def alphabet
       "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")

(def #^{:private true}
  test-data
  '(("t" "dA==")
    ("te" "dGU=")
    ("tes" "dGVz")
    ("test" "dGVzdA==")
    ("t\377\377t" "dP//dA==")
    ("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." "TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2ljaW5nIGVsaXQsIHNlZCBkbyBlaXVzbW9kIHRlbXBvciBpbmNpZGlkdW50IHV0IGxhYm9yZSBldCBkb2xvcmUgbWFnbmEgYWxpcXVhLiBVdCBlbmltIGFkIG1pbmltIHZlbmlhbSwgcXVpcyBub3N0cnVkIGV4ZXJjaXRhdGlvbiB1bGxhbWNvIGxhYm9yaXMgbmlzaSB1dCBhbGlxdWlwIGV4IGVhIGNvbW1vZG8gY29uc2VxdWF0LiBEdWlzIGF1dGUgaXJ1cmUgZG9sb3IgaW4gcmVwcmVoZW5kZXJpdCBpbiB2b2x1cHRhdGUgdmVsaXQgZXNzZSBjaWxsdW0gZG9sb3JlIGV1IGZ1Z2lhdCBudWxsYSBwYXJpYXR1ci4gRXhjZXB0ZXVyIHNpbnQgb2NjYWVjYXQgY3VwaWRhdGF0IG5vbiBwcm9pZGVudCwgc3VudCBpbiBjdWxwYSBxdWkgb2ZmaWNpYSBkZXNlcnVudCBtb2xsaXQgYW5pbSBpZCBlc3QgbGFib3J1bS4=")))


(defn decode
  "Decode sequence of base64 encoded characters to a sequence of
  characters."
  {:test #(doseq [[plain encoded] test-data]
            (is (= (decode (seq encoded)) (seq plain))))}
  [string]
  (if (empty? string)
    nil
    (let [t (take 4 (filter #(not (= \= %)) string))
          v (int (reduce #(+ (bit-shift-left (int %1) 6) (int %2))
                         (map #(. alphabet (indexOf (str %))) t)))
          r (map #(char (bit-and (bit-shift-right v %) 0xff))
                 ({2 '(4) 3 '(10 2) 4 '(16 8 0)} (count t)))]
      (concat r (lazy-seq (decode (drop 4 string)))))))

(defn decode-str [string]
  "Decode a base64 encoded string."
    (apply str (decode string)))
