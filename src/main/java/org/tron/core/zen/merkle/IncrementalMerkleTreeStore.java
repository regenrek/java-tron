package org.tron.core.zen.merkle;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tron.common.utils.ByteArray;
import org.tron.core.db.Manager;
import org.tron.core.db.TronStoreWithRevoking;

@Slf4j(topic = "DB")
@Component
public class IncrementalMerkleTreeStore
    extends TronStoreWithRevoking<IncrementalMerkleTreeCapsule> {

  @Autowired
  public IncrementalMerkleTreeStore(@Value("IncrementalMerkleTree") String dbName) {
    super(dbName);
  }

  @Override
  public IncrementalMerkleTreeCapsule get(byte[] key) {
    byte[] value = revokingDB.getUnchecked(key);
    return ArrayUtils.isEmpty(value) ? null : new IncrementalMerkleTreeCapsule(value);
  }

  public boolean contain(byte[] key) {
    byte[] value = revokingDB.getUnchecked(key);
    return !ArrayUtils.isEmpty(value);
  }

  public void addEmptyTree(){
    IncrementalMerkleTreeContainer container =
        (new IncrementalMerkleTreeCapsule()).toMerkleTreeContainer();

    put(container.getMerkleTreeKey(), container.getTreeCapsule());
    logger.info("addEmptyTree,root:" + ByteArray.toHexString(container.getMerkleTreeKey()));
  }
}
