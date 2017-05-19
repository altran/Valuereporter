package org.valuereporter.implemented;

import java.util.List;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public interface QueryOperations {
    List<ImplementedMethod> findImplementedMethods(String prefix, String name);
}
