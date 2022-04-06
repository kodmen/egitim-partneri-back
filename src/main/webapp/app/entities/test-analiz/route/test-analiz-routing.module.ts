import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TestAnalizComponent } from '../list/test-analiz.component';
import { TestAnalizDetailComponent } from '../detail/test-analiz-detail.component';
import { TestAnalizUpdateComponent } from '../update/test-analiz-update.component';
import { TestAnalizRoutingResolveService } from './test-analiz-routing-resolve.service';

const testAnalizRoute: Routes = [
  {
    path: '',
    component: TestAnalizComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TestAnalizDetailComponent,
    resolve: {
      testAnaliz: TestAnalizRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TestAnalizUpdateComponent,
    resolve: {
      testAnaliz: TestAnalizRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TestAnalizUpdateComponent,
    resolve: {
      testAnaliz: TestAnalizRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(testAnalizRoute)],
  exports: [RouterModule],
})
export class TestAnalizRoutingModule {}
