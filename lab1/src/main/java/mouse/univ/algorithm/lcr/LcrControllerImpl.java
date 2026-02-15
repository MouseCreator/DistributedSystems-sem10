package mouse.univ.algorithm.lcr;


import mouse.univ.algorithm.ControllerImpl;

import java.util.List;

public class LcrControllerImpl extends ControllerImpl<LcrMessage> implements LcrController {
    public LcrControllerImpl(List<Long> uids) {
        super(uids);
    }
}
