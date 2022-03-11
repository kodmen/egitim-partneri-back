import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SoruTestComponent } from '../list/soru-test.component';
import { SoruTestDetailComponent } from '../detail/soru-test-detail.component';
import { SoruTestUpdateComponent } from '../update/soru-test-update.component';
import { SoruTestRoutingResolveService } from './soru-test-routing-resolve.service';

const soruTestRoute: Routes = [
  {
    path: '',
    component: SoruTestComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SoruTestDetailComponent,
    resolve: {
      soruTest: SoruTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SoruTestUpdateComponent,
    resolve: {
      soruTest: SoruTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SoruTestUpdateComponent,
    resolve: {
      soruTest: SoruTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(soruTestRoute)],
  exports: [RouterModule],
})
export class SoruTestRoutingModule {}
