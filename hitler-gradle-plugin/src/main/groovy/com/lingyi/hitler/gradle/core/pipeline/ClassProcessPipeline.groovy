package com.lingyi.hitler.gradle.core.pipeline


/**
 * @author sunchuanwen
 * @time 2019/5/9.
 */
class ClassProcessPipeline implements Pipeline{

    Valve first
    Valve basic

    @Override
    Valve getFirst() {
        return first
    }

    @Override
    Valve getBasic() {
        return basic
    }

    @Override
    void setBasic(Valve valve) {
        this.basic = valve
    }

    @Override
    void addValve(Valve valve) {
        if (first == null) {
            first = valve
            valve.setNext(basic)
        } else {
            Valve current = first
            while (current != null) {
                if (current.getNext() == basic) {
                    current.setNext(valve)
                    valve.setNext(basic)
                    break
                }
                current = current.getNext()
            }
        }
    }
}